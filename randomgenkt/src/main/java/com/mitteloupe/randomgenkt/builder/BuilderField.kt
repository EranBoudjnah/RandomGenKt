package com.mitteloupe.randomgenkt.builder

import com.mitteloupe.randomgenkt.FieldDataProvider
import com.mitteloupe.randomgenkt.JavaDefaultValuesInstanceProvider
import com.mitteloupe.randomgenkt.KotlinDefaultValuesInstanceProvider
import com.mitteloupe.randomgenkt.RandomGen
import com.mitteloupe.randomgenkt.fielddataprovider.WeightedFieldDataProvidersFieldDataProvider
import com.mitteloupe.randomgenkt.model.InstanceProvider
import com.mitteloupe.randomgenkt.model.MutableDataProviderMap
import com.mitteloupe.randomgenkt.model.MutableDataProviderMap.ConfiguredFieldDataProvider
import kotlin.reflect.KClass

class BuilderField<GENERATED_INSTANCE : Any> : IncompleteBuilderField<GENERATED_INSTANCE> {
    private var lastWeight = 0.0
    private val lastFieldDataProvider get() = dataProviders[lastUsedFieldName]

    internal constructor(
        generatedInstanceClass: Class<GENERATED_INSTANCE>,
        incompleteBuilderField: IncompleteBuilderField<GENERATED_INSTANCE>
    ) : super(
        generatedInstanceClass,
        incompleteBuilderField.factory
    ) {
        copyFieldsFromIncompleteInstanceProvider(incompleteBuilderField)
    }

    internal constructor(
        generatedInstanceKClass: KClass<GENERATED_INSTANCE>,
        incompleteBuilderField: IncompleteBuilderField<GENERATED_INSTANCE>
    ) : super(
        generatedInstanceKClass,
        incompleteBuilderField.factory
    ) {
        copyFieldsFromIncompleteInstanceProvider(incompleteBuilderField)
    }

    internal constructor(
        instanceProvider: InstanceProvider<GENERATED_INSTANCE>,
        incompleteBuilderField: IncompleteBuilderField<GENERATED_INSTANCE>
    ) : super(
        instanceProvider,
        incompleteBuilderField.factory
    ) {
        copyFieldsFromIncompleteInstanceProvider(incompleteBuilderField)
    }

    override fun <FIELD_DATA_TYPE : Any> returning(
        fieldDataProvider: FieldDataProvider<GENERATED_INSTANCE, FIELD_DATA_TYPE>
    ) = if (wrappedInWeightedFieldDataProvider()) {
        addFieldDataProviderToWeightedFieldDataProvider(fieldDataProvider, lastWeight)
        this
    } else {
        super.returning(fieldDataProvider)
    }

    private fun copyFieldsFromIncompleteInstanceProvider(
        incompleteBuilderField: IncompleteBuilderField<GENERATED_INSTANCE>
    ) {
        dataProviders.putAll(incompleteBuilderField.dataProviders)
        onGenerateCallbacks.addAll(incompleteBuilderField.onGenerateCallbacks)
        lastUsedFieldName = incompleteBuilderField.lastUsedFieldName
        lastUsedDeclarationSite = incompleteBuilderField.lastUsedDeclarationSite

        if (incompleteBuilderField is BuilderField<*>) {
            lastWeight = (incompleteBuilderField as BuilderField<*>).lastWeight
        }
    }

    fun or() = orWithWeight(1.0)

    fun orWithWeight(weight: Double): BuilderReturnValue<GENERATED_INSTANCE> {
        wrapInWeightedFieldDataProviderIfNotWrapped()
        lastWeight = weight
        return builderReturnValueForInstance
    }

    fun build(): RandomGen<GENERATED_INSTANCE> {
        if (initializeType == InitializeType.WITH_JAVA_CLASS) {
            instanceProvider = JavaDefaultValuesInstanceProvider(generatedInstanceJavaClass)
        } else if (initializeType == InitializeType.WITH_KOTLIN_CLASS) {
            instanceProvider = KotlinDefaultValuesInstanceProvider(generatedInstanceKotlinClass)
        }

        val randomGen = RandomGen(
            instanceProvider,
            MutableDataProviderMap(dataProviders),
            onGenerateCallbacks.toList()
        )

        dataProviders.clear()
        onGenerateCallbacks.clear()

        return randomGen
    }

    private fun wrapInWeightedFieldDataProviderIfNotWrapped() {
        if (!wrappedInWeightedFieldDataProvider()) {
            lastFieldDataProvider?.let { configuredFieldDataProvider ->
                val wrapper = factory.getWeightedFieldDataProvidersFieldDataProvider(
                    configuredFieldDataProvider.fieldDataProvider
                )
                dataProviders[lastUsedFieldName] = ConfiguredFieldDataProvider(
                    wrapper,
                    configuredFieldDataProvider.declarationSite
                )
            }
        }
    }

    private fun wrappedInWeightedFieldDataProvider() = lastFieldDataProvider?.fieldDataProvider is
        WeightedFieldDataProvidersFieldDataProvider<*, *>

    private fun <FIELD_DATA_TYPE : Any> addFieldDataProviderToWeightedFieldDataProvider(
        fieldDataProvider: FieldDataProvider<GENERATED_INSTANCE, FIELD_DATA_TYPE>,
        weight: Double
    ) {
        @Suppress("UNCHECKED_CAST")
        val qualifiedLastFieldDataProvider = lastFieldDataProvider?.fieldDataProvider
            as WeightedFieldDataProvidersFieldDataProvider<GENERATED_INSTANCE, FIELD_DATA_TYPE>

        qualifiedLastFieldDataProvider.addFieldDataProvider(fieldDataProvider, weight)
    }
}
