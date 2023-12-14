package com.mitteloupe.randomgenkt.builder

import com.mitteloupe.randomgenkt.FieldDataProvider
import com.mitteloupe.randomgenkt.FieldDataProviderFactory
import com.mitteloupe.randomgenkt.RandomGen
import com.mitteloupe.randomgenkt.builder.IncompleteBuilderField.InitializeType.WITH_INSTANCE_PROVIDER
import com.mitteloupe.randomgenkt.builder.IncompleteBuilderField.InitializeType.WITH_JAVA_CLASS
import com.mitteloupe.randomgenkt.builder.IncompleteBuilderField.InitializeType.WITH_KOTLIN_CLASS
import com.mitteloupe.randomgenkt.model.DeclarationSite
import com.mitteloupe.randomgenkt.model.InstanceProvider
import com.mitteloupe.randomgenkt.model.MutableDataProviderMap
import com.mitteloupe.randomgenkt.model.MutableDataProviderMap.ConfiguredFieldDataProvider
import kotlin.reflect.KClass

open class IncompleteBuilderField<GENERATED_INSTANCE : Any> private constructor(
    internal val factory: FieldDataProviderFactory<GENERATED_INSTANCE>
) {
    internal constructor(
        generatedInstanceClass: Class<GENERATED_INSTANCE>,
        factory: FieldDataProviderFactory<GENERATED_INSTANCE>
    ) : this(factory) {
        this.generatedInstanceJavaClass = generatedInstanceClass
        initializeType = WITH_JAVA_CLASS
    }

    internal constructor(
        generatedInstanceKClass: KClass<GENERATED_INSTANCE>,
        factory: FieldDataProviderFactory<GENERATED_INSTANCE>
    ) : this(factory) {
        this.generatedInstanceKotlinClass = generatedInstanceKClass
        initializeType = WITH_KOTLIN_CLASS
    }

    internal constructor(
        instanceProvider: InstanceProvider<GENERATED_INSTANCE>,
        factory: FieldDataProviderFactory<GENERATED_INSTANCE>
    ) : this(factory) {
        this.instanceProvider = instanceProvider
        initializeType = WITH_INSTANCE_PROVIDER
    }

    internal val dataProviders: MutableDataProviderMap<GENERATED_INSTANCE> =
        MutableDataProviderMap()
    internal val onGenerateCallbacks: MutableList<RandomGen.OnGenerateCallback<GENERATED_INSTANCE>>

    internal lateinit var instanceProvider: InstanceProvider<GENERATED_INSTANCE>
    internal lateinit var initializeType: InitializeType
    internal lateinit var generatedInstanceJavaClass: Class<GENERATED_INSTANCE>
    internal lateinit var generatedInstanceKotlinClass: KClass<GENERATED_INSTANCE>
    internal lateinit var lastUsedFieldName: String
    internal lateinit var lastUsedDeclarationSite: DeclarationSite

    internal val builderReturnValueForInstance: BuilderReturnValue<GENERATED_INSTANCE>
        get() = BuilderReturnValue(
            when (initializeType) {
                WITH_JAVA_CLASS -> BuilderField(generatedInstanceJavaClass, this)
                WITH_KOTLIN_CLASS -> BuilderField(generatedInstanceKotlinClass, this)
                WITH_INSTANCE_PROVIDER -> BuilderField(instanceProvider, this)
            },
            factory
        )

    init {
        onGenerateCallbacks = ArrayList()
    }

    fun withField(
        fieldName: String,
        declarationSite: DeclarationSite = DeclarationSite.AllSites
    ): BuilderReturnValue<GENERATED_INSTANCE> {
        lastUsedFieldName = fieldName
        lastUsedDeclarationSite = declarationSite

        return builderReturnValueForInstance
    }

    internal open fun <FIELD_DATA_TYPE : Any> returning(
        fieldDataProvider: FieldDataProvider<GENERATED_INSTANCE, FIELD_DATA_TYPE>
    ): IncompleteBuilderField<GENERATED_INSTANCE> {
        dataProviders[lastUsedFieldName] =
            ConfiguredFieldDataProvider(fieldDataProvider, lastUsedDeclarationSite)
        return this
    }

    fun onGenerate(
        onGenerateCallback: RandomGen.OnGenerateCallback<GENERATED_INSTANCE>
    ): IncompleteBuilderField<GENERATED_INSTANCE> {
        onGenerateCallbacks.add(onGenerateCallback)
        return this
    }

    internal enum class InitializeType {
        WITH_JAVA_CLASS,
        WITH_KOTLIN_CLASS,
        WITH_INSTANCE_PROVIDER
    }
}
