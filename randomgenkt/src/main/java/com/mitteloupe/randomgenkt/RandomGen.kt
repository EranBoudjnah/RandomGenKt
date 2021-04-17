package com.mitteloupe.randomgenkt

import com.mitteloupe.randomgenkt.fielddataprovider.WeightedFieldDataProvidersFieldDataProvider
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashMap
import java.util.Random
import java.util.UUID
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

/**
 * Created by Eran Boudjnah on 24/04/2018.
 */
class RandomGen<GENERATED_INSTANCE> private constructor(
    private val instanceProvider: () -> GENERATED_INSTANCE,
    private val dataProviders: Map<String, (GENERATED_INSTANCE?) -> Any>,
    private val onGenerateCallbacks: List<OnGenerateCallback<GENERATED_INSTANCE>>
) : (Any?) -> GENERATED_INSTANCE {
    private val fields by lazy { getAllFields() }

    fun generate(): GENERATED_INSTANCE = invoke(null)

    override fun invoke(instance: Any?): GENERATED_INSTANCE {
        val providedInstance = instanceProvider()

        setAllFields(providedInstance)

        notifyOnGenerateCallbacks(providedInstance)

        return providedInstance
    }

    private fun setAllFields(instance: GENERATED_INSTANCE) {
        dataProviders.forEach { (fieldName, fieldDataProvider) ->
            if (!fields.containsKey(fieldName)) {
                throw IllegalArgumentException("Cannot set field $fieldName - field not found")
            }

            try {
                fields[fieldName]?.let { field ->
                    val generatedValue = fieldDataProvider(instance)
                    setField(instance, field, generatedValue)
                }
            } catch (exception: AssignmentException) {
                throw IllegalArgumentException(
                    "Cannot set field $fieldName due to invalid value",
                    exception
                )
            }
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun setField(instance: GENERATED_INSTANCE, field: Field, value: Any?) {
        val valueToSet = when {
            isFieldArray(field) -> getValueAsArray(field, value)
            isLazy(field) -> getValueAsLazy(value)
            else -> value
        }

        setFieldToRawValue(instance, field, valueToSet)
    }

    private fun isFieldArray(field: Field) = field.type.isArray

    private fun isLazy(field: Field) = Lazy::class.java.isAssignableFrom(field.type)

    private fun isCollection(value: Any?) = value?.let {
        Collection::class.java.isAssignableFrom(value.javaClass)
    } ?: false

    private fun getValueAsArray(field: Field, value: Any?): Any {
        if (!isCollection(value)) {
            throw AssignmentException(RuntimeException("Expected collection value"))
        }

        val valueAsList = ArrayList(value as List<Any?>)

        try {
            val genericType = field.type.componentType as Class<*>
            return valueAsList.toArray(TypedArray(genericType, valueAsList.size).get())
        } catch (exception: ArrayStoreException) {
            throw AssignmentException(exception)
        }
    }

    private fun <ValueType> getValueAsLazy(value: ValueType?) = lazy { value }

    private fun setFieldToRawValue(instance: GENERATED_INSTANCE, field: Field, value: Any?) =
        try {
            field.set(instance, value)
        } catch (exception: IllegalAccessException) {
            throw AssignmentException(exception)
        } catch (exception: IllegalArgumentException) {
            throw AssignmentException(exception)
        }

    private fun notifyOnGenerateCallbacks(instance: GENERATED_INSTANCE) {
        onGenerateCallbacks.forEach { onGenerateCallback ->
            onGenerateCallback.onGenerate(instance)
        }
    }

    private fun getAllFields(): Map<String, Field> {
        val fields: MutableMap<String, Field> = HashMap()
        val instance = instanceProvider()
        val allProperties = (instance as Any)::class.declaredMemberProperties
        allProperties.forEach { property ->
            property.javaField?.let { field ->
                if (Modifier.isPrivate(field.modifiers)) {
                    field.isAccessible = true
                }

                fields[property.name] = field
            }
        }

        return fields
    }

    class Builder<GENERATED_INSTANCE> {
        inline fun <reified CLASS_TYPE : GENERATED_INSTANCE> ofClass(): IncompleteBuilderField<GENERATED_INSTANCE> =
            @Suppress("UNCHECKED_CAST")
            ofClass(CLASS_TYPE::class.java as Class<GENERATED_INSTANCE>)

        fun ofClass(generatedInstanceClass: Class<GENERATED_INSTANCE>): IncompleteBuilderField<GENERATED_INSTANCE> =
            IncompleteBuilderField(generatedInstanceClass, DefaultFieldDataProviderFactory())

        internal inline fun <reified CLASS_TYPE : GENERATED_INSTANCE> ofClassWithFactory(factory: FieldDataProviderFactory<GENERATED_INSTANCE>) =
            @Suppress("UNCHECKED_CAST")
            IncompleteBuilderField(CLASS_TYPE::class.java as Class<GENERATED_INSTANCE>, factory)

        fun withProvider(instanceProvider: () -> GENERATED_INSTANCE): IncompleteBuilderField<GENERATED_INSTANCE> =
            IncompleteBuilderField(instanceProvider, DefaultFieldDataProviderFactory())

        internal fun withFactoryAndProvider(
            factory: FieldDataProviderFactory<GENERATED_INSTANCE>,
            instanceProvider: () -> GENERATED_INSTANCE
        ) = IncompleteBuilderField(instanceProvider, factory)

        private class DefaultFieldDataProviderFactory<GENERATED_INSTANCE> :
            SimpleFieldDataProviderFactory<GENERATED_INSTANCE>(Random(), DefaultUuidGenerator())

        private class DefaultUuidGenerator : UuidGenerator {
            override fun randomUUID() = UUID.randomUUID().toString()
        }
    }

    class BuilderField<GENERATED_INSTANCE> : IncompleteBuilderField<GENERATED_INSTANCE> {
        private var lastWeight = 0.0
        private val lastFieldDataProvider get() = dataProviders[lastUsedFieldName]

        internal constructor(
            generatedInstanceClass: Class<GENERATED_INSTANCE>,
            incompleteBuilderField: IncompleteBuilderField<GENERATED_INSTANCE>
        ) : super(generatedInstanceClass, incompleteBuilderField.factory) {

            copyFieldsFromIncompleteInstanceProvider(incompleteBuilderField)
        }

        internal constructor(
            instanceProvider: () -> GENERATED_INSTANCE,
            incompleteBuilderField: IncompleteBuilderField<GENERATED_INSTANCE>
        ) : super(instanceProvider, incompleteBuilderField.factory) {

            copyFieldsFromIncompleteInstanceProvider(incompleteBuilderField)
        }

        override fun <FIELD_DATA_TYPE> returning(fieldDataProvider: (GENERATED_INSTANCE?) -> FIELD_DATA_TYPE) =
            when {
                wrappedInWeightedFieldDataProvider() -> {
                    addFieldDataProviderToWeightedFieldDataProvider(fieldDataProvider, lastWeight)
                    this
                }
                else -> super.returning(fieldDataProvider)
            }

        private fun copyFieldsFromIncompleteInstanceProvider(incompleteBuilderField: IncompleteBuilderField<GENERATED_INSTANCE>) {
            dataProviders.putAll(incompleteBuilderField.dataProviders)
            onGenerateCallbacks.addAll(incompleteBuilderField.onGenerateCallbacks)
            lastUsedFieldName = incompleteBuilderField.lastUsedFieldName

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
            if (initializeType == InitializeType.WITH_CLASS) {
                instanceProvider = DefaultValuesInstanceProvider(generatedInstanceClass)
            }

            val randomGen = RandomGen(
                instanceProvider,
                LinkedHashMap(dataProviders),
                ArrayList(onGenerateCallbacks)
            )

            dataProviders.clear()
            onGenerateCallbacks.clear()

            return randomGen
        }

        private fun wrapInWeightedFieldDataProviderIfNotWrapped() {
            if (!wrappedInWeightedFieldDataProvider()) {
                lastFieldDataProvider?.let {
                    val wrapper = factory.getWeightedFieldDataProvidersFieldDataProvider(it)
                    dataProviders[lastUsedFieldName] = wrapper
                }
            }
        }

        private fun wrappedInWeightedFieldDataProvider() =
            lastFieldDataProvider is WeightedFieldDataProvidersFieldDataProvider<*, *>

        private fun <FIELD_DATA_TYPE> addFieldDataProviderToWeightedFieldDataProvider(
            fieldDataProvider: (GENERATED_INSTANCE?) -> FIELD_DATA_TYPE,
            weight: Double
        ) {
            @Suppress("UNCHECKED_CAST")
            val qualifiedLastFieldDataProvider =
                lastFieldDataProvider as WeightedFieldDataProvidersFieldDataProvider<GENERATED_INSTANCE, FIELD_DATA_TYPE>

            qualifiedLastFieldDataProvider.addFieldDataProvider(fieldDataProvider, weight)
        }
    }

    open class IncompleteBuilderField<GENERATED_INSTANCE>
    private constructor(
        internal val factory: FieldDataProviderFactory<GENERATED_INSTANCE>
    ) {
        internal val dataProviders: MutableMap<String, (GENERATED_INSTANCE?) -> Any>
        internal val onGenerateCallbacks: MutableList<OnGenerateCallback<GENERATED_INSTANCE>>

        internal lateinit var instanceProvider: () -> GENERATED_INSTANCE
        internal lateinit var initializeType: InitializeType
        internal lateinit var generatedInstanceClass: Class<GENERATED_INSTANCE>
        internal lateinit var lastUsedFieldName: String

        internal val builderReturnValueForInstance: BuilderReturnValue<GENERATED_INSTANCE>
            get() = BuilderReturnValue(
                when (initializeType) {
                    InitializeType.WITH_CLASS -> BuilderField(generatedInstanceClass, this)
                    else -> BuilderField(instanceProvider, this)
                },
                factory
            )

        internal constructor(
            generatedInstanceClass: Class<GENERATED_INSTANCE>,
            factory: FieldDataProviderFactory<GENERATED_INSTANCE>
        ) : this(factory) {

            this.generatedInstanceClass = generatedInstanceClass
            initializeType = InitializeType.WITH_CLASS
        }

        internal constructor(
            instanceProvider: () -> GENERATED_INSTANCE,
            factory: FieldDataProviderFactory<GENERATED_INSTANCE>
        ) : this(factory) {

            this.instanceProvider = instanceProvider
            initializeType = InitializeType.WITH_INSTANCE_PROVIDER
        }

        init {
            dataProviders = LinkedHashMap()
            onGenerateCallbacks = ArrayList()
        }

        fun withField(fieldName: String): BuilderReturnValue<GENERATED_INSTANCE> {
            lastUsedFieldName = fieldName

            return builderReturnValueForInstance
        }

        internal open fun <FIELD_DATA_TYPE> returning(fieldDataProvider: (GENERATED_INSTANCE?) -> FIELD_DATA_TYPE): IncompleteBuilderField<GENERATED_INSTANCE> {
            @Suppress("UNCHECKED_CAST")
            dataProviders[lastUsedFieldName] = fieldDataProvider as (GENERATED_INSTANCE?) -> Any
            return this
        }

        fun onGenerate(onGenerateCallback: OnGenerateCallback<GENERATED_INSTANCE>): IncompleteBuilderField<GENERATED_INSTANCE> {
            onGenerateCallbacks.add(onGenerateCallback)
            return this
        }

        internal enum class InitializeType {
            WITH_CLASS,
            WITH_INSTANCE_PROVIDER
        }
    }

    class BuilderReturnValue<RETURN_TYPE> internal constructor(
        private val builderField: BuilderField<RETURN_TYPE>,
        private val factory: FieldDataProviderFactory<RETURN_TYPE>
    ) {

        fun <VALUE_TYPE> returningExplicitly(value: VALUE_TYPE) = getBuilderFieldFromIncomplete(
            builderField.returning(factory.getExplicitFieldDataProvider(value))
        )

        fun <VALUE_TYPE> returning(list: List<VALUE_TYPE>): BuilderField<RETURN_TYPE> {
            val immutableList = ArrayList(list)
            return getBuilderFieldFromIncomplete(
                builderField.returning(factory.getGenericListFieldDataProvider(immutableList))
            )
        }

        fun returningBoolean() =
            getBuilderFieldFromIncomplete(builderField.returning(factory.booleanFieldDataProvider))

        fun returningByte() =
            getBuilderFieldFromIncomplete(builderField.returning(factory.byteFieldDataProvider))

        fun returningBytes(size: Int) = getBuilderFieldFromIncomplete(
            builderField.returning(factory.getByteListFieldDataProvider(size))
        )

        fun returningBytes(minSize: Int, maxSize: Int) = getBuilderFieldFromIncomplete(
            builderField.returning(factory.getByteListFieldDataProvider(minSize, maxSize))
        )

        fun returningDouble() =
            getBuilderFieldFromIncomplete(builderField.returning(factory.getDoubleFieldDataProvider()))

        fun returningFloat() =
            getBuilderFieldFromIncomplete(builderField.returning(factory.getFloatFieldDataProvider()))

        fun returningInt() =
            getBuilderFieldFromIncomplete(builderField.returning(factory.getIntFieldDataProvider()))

        fun returningLong() =
            getBuilderFieldFromIncomplete(builderField.returning(factory.getLongFieldDataProvider()))

        fun returningShort() =
            getBuilderFieldFromIncomplete(builderField.returning(factory.getShortFieldDataProvider()))

        fun returning(minimum: Double, maximum: Double) = getBuilderFieldFromIncomplete(
            builderField.returning(factory.getDoubleFieldDataProvider(minimum, maximum))
        )

        fun returning(minimum: Float, maximum: Float) = getBuilderFieldFromIncomplete(
            builderField.returning(factory.getFloatFieldDataProvider(minimum, maximum))
        )

        fun returning(minimum: Int, maximum: Int) = getBuilderFieldFromIncomplete(
            builderField.returning(factory.getIntFieldDataProvider(minimum, maximum))
        )

        fun returning(minimum: Long, maximum: Long) = getBuilderFieldFromIncomplete(
            builderField.returning(factory.getLongFieldDataProvider(minimum, maximum))
        )

        fun returning(minimum: Short, maximum: Short) = getBuilderFieldFromIncomplete(
            builderField.returning(factory.getShortFieldDataProvider(minimum, maximum))
        )

        fun returningSequentialInteger() =
            getBuilderFieldFromIncomplete(builderField.returning(factory.sequentialIntegerFieldDataProvider))

        fun returningSequentialInteger(startValue: Int) = getBuilderFieldFromIncomplete(
            builderField.returning(factory.getSequentialIntegerFieldDataProvider(startValue))
        )

        fun returningUuid() =
            getBuilderFieldFromIncomplete(builderField.returning(factory.uuidFieldDataProvider))

        fun returningRgb(alpha: Boolean) = getBuilderFieldFromIncomplete(
            builderField.returning(factory.getRgbFieldDataProvider(alpha))
        )

        /**
         * See [.returningLoremIpsum].
         *
         * @return builder generating one copy of the Lorem Ipsum text
         */
        fun returningLoremIpsum() =
            getBuilderFieldFromIncomplete(builderField.returning(factory.loremIpsumFieldDataProvider))

        /**
         * See [.returningLoremIpsum].
         *
         * @param length The number of characters to return
         * @return A builder generating a substring of an infinite (well, kind of) Lorem Ipsum text
         */
        fun returningLoremIpsum(length: Int) = getBuilderFieldFromIncomplete(
            builderField.returning(factory.getLoremIpsumFieldDataProvider(length))
        )

        /**
         * See [.returningLoremIpsum].
         *
         * @param minLength The minimal number of characters to return
         * @param maxLength The maximal number of characters to return
         * @return A builder generating a substring of an infinite (well, kind of) Lorem Ipsum text
         */
        fun returningLoremIpsum(minLength: Int, maxLength: Int) = getBuilderFieldFromIncomplete(
            builderField.returning(factory.getLoremIpsumFieldDataProvider(minLength, maxLength))
        )

        /**
         * Adds a String containing Lorem Ipsum. Length determines how many characters of Lorem Ipsum to return. The content will repeat itself if
         * the requested length exceeds the length of Lorem Ipsum.
         *
         * @param minLength The minimal number of characters to return
         * @param maxLength The maximal number of characters to return
         * @param paragraphDelimiter The string to use between Lorem Ipsum paragraphs
         * @return A builder generating a substring of an infinite (well, kind of) Lorem Ipsum text
         */
        fun returningLoremIpsum(minLength: Int, maxLength: Int, paragraphDelimiter: String) =
            getBuilderFieldFromIncomplete(
                builderField.returning(
                    factory.getLoremIpsumFieldDataProvider(minLength, maxLength, paragraphDelimiter)
                )
            )

        /**
         * Adds a generator of random enum values for the given field.
         *
         * @param &lt;ENUM_TYPE&gt; Implicit. The enum type to use
         * @return A builder with a data provider
         */
        inline fun <reified ENUM_TYPE : Enum<*>> returning() =
            returning(ENUM_TYPE::class.java)

        fun <ENUM_TYPE : Enum<*>> returning(enumClass: Class<ENUM_TYPE>) =
            getBuilderFieldFromIncomplete(
                builderField.returning(factory.getRandomEnumFieldDataProvider(enumClass))
            )

        /**
         * Adds a lambda generated value for the given field.
         *
         * @param fieldDataProvider An instance of the lambda to use
         * @param &lt;VALUE_TYPE&gt;      The type returned from the lambda
         * @return An instance of the specified `VALUE_TYPE`
         */
        fun <VALUE_TYPE> returning(fieldDataProvider: (RETURN_TYPE?) -> VALUE_TYPE) =
            getBuilderFieldFromIncomplete(builderField.returning(fieldDataProvider))

        fun <VALUE_TYPE> returning(
            instances: Int,
            fieldDataProvider: (RETURN_TYPE?) -> VALUE_TYPE
        ) = getBuilderFieldFromIncomplete(
            builderField.returning(
                factory.getCustomListFieldDataProvider(instances, fieldDataProvider)
            )
        )

        fun <VALUE_TYPE> returning(
            minInstances: Int,
            maxInstances: Int,
            fieldDataProvider: (RETURN_TYPE?) -> VALUE_TYPE
        ) = getBuilderFieldFromIncomplete(
            builderField.returning(
                factory.getCustomListRangeFieldDataProvider(
                    minInstances,
                    maxInstances,
                    fieldDataProvider
                )
            )
        )

        private fun getBuilderFieldFromIncomplete(incompleteBuilderField: IncompleteBuilderField<RETURN_TYPE>) =
            if (incompleteBuilderField.initializeType == IncompleteBuilderField.InitializeType.WITH_CLASS) {
                BuilderField(incompleteBuilderField.generatedInstanceClass, incompleteBuilderField)
            } else {
                BuilderField(incompleteBuilderField.instanceProvider, incompleteBuilderField)
            }

        private fun initializedWithClass(incompleteBuilderField: IncompleteBuilderField<RETURN_TYPE>) =
            incompleteBuilderField.initializeType == IncompleteBuilderField.InitializeType.WITH_CLASS
    }

    private inner class TypedArray<ELEMENT_TYPE>(elementClass: Class<ELEMENT_TYPE>, capacity: Int) {
        private val typedArray: Array<ELEMENT_TYPE>

        init {
            // Use Array native method to create array of a type only known at run time
            val newInstance = java.lang.reflect.Array.newInstance(elementClass, capacity)

            @Suppress("UNCHECKED_CAST")
            typedArray = newInstance as Array<ELEMENT_TYPE>
        }

        fun get() = typedArray
    }

    fun interface OnGenerateCallback<INSTANCE_TYPE> {
        fun onGenerate(generatedInstance: INSTANCE_TYPE)
    }

    private class AssignmentException(exception: Exception) : RuntimeException(exception)
}
