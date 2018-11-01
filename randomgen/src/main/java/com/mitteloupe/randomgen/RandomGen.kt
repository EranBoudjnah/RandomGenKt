package com.mitteloupe.randomgen

import com.mitteloupe.randomgen.fielddataprovider.WeightedFieldDataProvidersFieldDataProvider
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
	private val dataProviders: Map<String, FieldDataProvider<GENERATED_INSTANCE, *>>,
	private val onGenerateCallbacks: List<OnGenerateCallback<GENERATED_INSTANCE>>
) : FieldDataProvider<Any, GENERATED_INSTANCE> {
	private val fields: MutableMap<String, Field>

	init {
		fields = HashMap()

		getAllFields()
	}

	fun generate(): GENERATED_INSTANCE {
		return generate(null)
	}

	override fun generate(instance: Any?): GENERATED_INSTANCE {
		val providedInstance = instanceProvider.invoke()

		setAllFields(providedInstance)

		notifyOnGenerateCallbacks(providedInstance)

		return providedInstance
	}

	private fun setAllFields(instance: GENERATED_INSTANCE) {
		for ((fieldName, fieldDataProvider) in dataProviders) {
			if (!fields.containsKey(fieldName)) {
				throw IllegalArgumentException("Cannot set field $fieldName - field not found")
			}

			try {
				fields[fieldName]?.let { field ->
					val generatedValue = fieldDataProvider.generate(instance)
					setField(instance, field, generatedValue)
				}

			} catch (exception: AssignmentException) {
				throw IllegalArgumentException("Cannot set field $fieldName due to invalid value", exception)
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

	private fun isFieldArray(field: Field): Boolean {
		return field.type.isArray
	}

	private fun isLazy(field: Field): Boolean {
		return Lazy::class.java.isAssignableFrom(field.type)
	}

	private fun isCollection(value: Any?) = Collection::class.java.isAssignableFrom(value?.javaClass)

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

	private fun <ValueType> getValueAsLazy(value: ValueType?): Lazy<ValueType?> {
		return lazy { value }
	}

	private fun setFieldToRawValue(instance: GENERATED_INSTANCE, field: Field, value: Any?) {
		try {
			field.set(instance, value)
		} catch (exception: IllegalAccessException) {
			throw AssignmentException(exception)
		} catch (exception: IllegalArgumentException) {
			throw AssignmentException(exception)
		}

	}

	private fun notifyOnGenerateCallbacks(instance: GENERATED_INSTANCE) {
		for (onGenerateCallbacks in onGenerateCallbacks) {
			onGenerateCallbacks.onGenerate(instance)
		}
	}

	private fun getAllFields() {
		val instance = instanceProvider.invoke()
		val allProperties = (instance as Any)::class.declaredMemberProperties
		for (property in allProperties) {
			val maybeField = property.javaField
			maybeField?.let { field ->
				if (Modifier.isPrivate(field.modifiers)) {
					field.isAccessible = true
				}

				fields[property.name] =	field
			}
		}
	}

	class Builder<GENERATED_INSTANCE> {
		fun ofClass(generatedInstanceClass: Class<GENERATED_INSTANCE>): IncompleteBuilderField<GENERATED_INSTANCE> {
			return IncompleteBuilderField(generatedInstanceClass, DefaultFieldDataProviderFactory())
		}

		internal fun ofClassWithFactory(generatedInstanceClass: Class<GENERATED_INSTANCE>,
		                                factory: FieldDataProviderFactory<GENERATED_INSTANCE>): IncompleteBuilderField<GENERATED_INSTANCE> {
			return IncompleteBuilderField(generatedInstanceClass, factory)
		}

		fun withProvider(instanceProvider: () -> GENERATED_INSTANCE): IncompleteBuilderField<GENERATED_INSTANCE> {
			return IncompleteBuilderField(instanceProvider, DefaultFieldDataProviderFactory())
		}

		internal fun withFactoryAndProvider(factory: FieldDataProviderFactory<GENERATED_INSTANCE>,
		                                    instanceProvider: () -> GENERATED_INSTANCE): IncompleteBuilderField<GENERATED_INSTANCE> {
			return IncompleteBuilderField(instanceProvider, factory)
		}

		private class DefaultFieldDataProviderFactory<GENERATED_INSTANCE> : SimpleFieldDataProviderFactory<GENERATED_INSTANCE>(Random(), DefaultUuidGenerator())

		private class DefaultUuidGenerator : UuidGenerator {
			override fun randomUUID(): String {
				return UUID.randomUUID().toString()
			}
		}
	}

	class BuilderField<GENERATED_INSTANCE> : IncompleteBuilderField<GENERATED_INSTANCE> {
		private var lastWeight: Double = 0.toDouble()

		internal constructor(generatedInstanceClass: Class<GENERATED_INSTANCE>,
		                     incompleteBuilderField: IncompleteBuilderField<GENERATED_INSTANCE>) : super(generatedInstanceClass, incompleteBuilderField.factory) {

			copyFieldsFromIncompleteInstanceProvider(incompleteBuilderField)
		}

		internal constructor(instanceProvider: () -> GENERATED_INSTANCE,
		                     incompleteBuilderField: IncompleteBuilderField<GENERATED_INSTANCE>) : super(instanceProvider, incompleteBuilderField.factory) {

			copyFieldsFromIncompleteInstanceProvider(incompleteBuilderField)
		}

		override fun <FIELD_DATA_TYPE> returning(fieldDataProvider: FieldDataProvider<GENERATED_INSTANCE, FIELD_DATA_TYPE>): IncompleteBuilderField<GENERATED_INSTANCE> =
			if (wrappedInWeightedFieldDataProvider()) {
				addFieldDataProviderToWeightedFieldDataProvider(fieldDataProvider, lastWeight)
				this

			} else {
				super.returning(fieldDataProvider)
			}

		private fun copyFieldsFromIncompleteInstanceProvider(incompleteBuilderField: IncompleteBuilderField<GENERATED_INSTANCE>) {
			dataProviders.putAll(incompleteBuilderField.dataProviders)
			onGenerateCallbacks.addAll(incompleteBuilderField.onGenerateCallbacks)
			lastUsedFieldName = incompleteBuilderField.lastUsedFieldName

			if (incompleteBuilderField is BuilderField<*>) {
				lastWeight = (incompleteBuilderField as BuilderField<*>).lastWeight
			}
		}

		fun or(): BuilderReturnValue<GENERATED_INSTANCE> {
			return orWithWeight(1.0)
		}

		fun orWithWeight(weight: Double): BuilderReturnValue<GENERATED_INSTANCE> {
			wrapInWeightedFieldDataProviderIfNotWrapped()
			lastWeight = weight
			return builderReturnValueForInstance
		}

		fun build(): RandomGen<GENERATED_INSTANCE> {
			if (initializeType == InitializeType.WITH_CLASS) {
				instanceProvider = DefaultValuesInstanceProvider(generatedInstanceClass)
			}

			val randomGen = RandomGen(instanceProvider,
				HashMap(dataProviders),
				ArrayList(onGenerateCallbacks))

			dataProviders.clear()
			onGenerateCallbacks.clear()

			return randomGen
		}

		private fun wrapInWeightedFieldDataProviderIfNotWrapped() {
			val lastFieldDataProvider = dataProviders[lastUsedFieldName]

			if (!wrappedInWeightedFieldDataProvider()) {
				lastFieldDataProvider?.let {
					val wrapper = factory.getWeightedFieldDataProvidersFieldDataProvider(it)
					dataProviders[lastUsedFieldName] = wrapper
				}
			}
		}

		private fun wrappedInWeightedFieldDataProvider(): Boolean {
			val lastFieldDataProvider = dataProviders[lastUsedFieldName]

			return lastFieldDataProvider is WeightedFieldDataProvidersFieldDataProvider<*, *>
		}

		private fun <FIELD_DATA_TYPE> addFieldDataProviderToWeightedFieldDataProvider(
			fieldDataProvider: FieldDataProvider<GENERATED_INSTANCE, FIELD_DATA_TYPE>,
			weight: Double
		) {
			val lastFieldDataProvider = dataProviders[lastUsedFieldName]

			@Suppress("UNCHECKED_CAST")
			val qualifiedLastFieldDataProvider = lastFieldDataProvider as WeightedFieldDataProvidersFieldDataProvider<GENERATED_INSTANCE, FIELD_DATA_TYPE>

			qualifiedLastFieldDataProvider.addFieldDataProvider(fieldDataProvider, weight)
		}
	}

	open class IncompleteBuilderField<GENERATED_INSTANCE>
	private constructor(
		internal val factory: FieldDataProviderFactory<GENERATED_INSTANCE>
	) {
		internal val dataProviders: MutableMap<String, FieldDataProvider<GENERATED_INSTANCE, *>>
		internal val onGenerateCallbacks: MutableList<OnGenerateCallback<GENERATED_INSTANCE>>

		internal lateinit var instanceProvider: () -> GENERATED_INSTANCE
		internal lateinit var initializeType: InitializeType
		internal lateinit var generatedInstanceClass: Class<GENERATED_INSTANCE>
		internal lateinit var lastUsedFieldName: String

		internal val builderReturnValueForInstance: BuilderReturnValue<GENERATED_INSTANCE>
			get() = when (initializeType) {
				InitializeType.WITH_CLASS -> BuilderReturnValue(BuilderField(generatedInstanceClass, this), factory)
				else -> BuilderReturnValue(BuilderField(instanceProvider, this), factory)
			}

		internal constructor(generatedInstanceClass: Class<GENERATED_INSTANCE>,
		                     factory: FieldDataProviderFactory<GENERATED_INSTANCE>) : this(factory) {

			this.generatedInstanceClass = generatedInstanceClass
			initializeType = InitializeType.WITH_CLASS
		}

		internal constructor(instanceProvider: () -> GENERATED_INSTANCE,
		                     factory: FieldDataProviderFactory<GENERATED_INSTANCE>) : this(factory) {

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

		internal open fun <FIELD_DATA_TYPE> returning(fieldDataProvider: FieldDataProvider<GENERATED_INSTANCE, FIELD_DATA_TYPE>): IncompleteBuilderField<GENERATED_INSTANCE> {
			dataProviders[lastUsedFieldName] = fieldDataProvider
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

	class BuilderReturnValue<RETURN_TYPE> internal constructor(private val builderField: BuilderField<RETURN_TYPE>,
	                                                           private val factory: FieldDataProviderFactory<RETURN_TYPE>) {

		fun <VALUE_TYPE> returningExplicitly(value: VALUE_TYPE): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.getExplicitFieldDataProvider(value)))
		}

		fun <VALUE_TYPE> returning(list: List<VALUE_TYPE>): BuilderField<RETURN_TYPE> {
			val immutableList = ArrayList(list)
			return getBuilderFieldFromIncomplete(builderField.returning(factory.getGenericListFieldDataProvider(immutableList)))
		}

		fun returningBoolean(): BuilderField<RETURN_TYPE> =
			getBuilderFieldFromIncomplete(builderField.returning(factory.booleanFieldDataProvider))

		fun returningByte(): BuilderField<RETURN_TYPE> =
			getBuilderFieldFromIncomplete(builderField.returning(factory.byteFieldDataProvider))

		fun returningBytes(size: Int): BuilderField<RETURN_TYPE> =
			getBuilderFieldFromIncomplete(builderField.returning(factory.getByteListFieldDataProvider(size)))

		fun returningBytes(minSize: Int, maxSize: Int): BuilderField<RETURN_TYPE> =
			getBuilderFieldFromIncomplete(builderField.returning(factory.getByteListFieldDataProvider(minSize, maxSize)))

		fun returningDouble(): BuilderField<RETURN_TYPE> = getBuilderFieldFromIncomplete(builderField.returning(factory.getDoubleFieldDataProvider()))

		fun returningFloat(): BuilderField<RETURN_TYPE> = getBuilderFieldFromIncomplete(builderField.returning(factory.getFloatFieldDataProvider()))

		fun returningInt(): BuilderField<RETURN_TYPE> = getBuilderFieldFromIncomplete(builderField.returning(factory.getIntFieldDataProvider()))

		fun returningLong(): BuilderField<RETURN_TYPE> = getBuilderFieldFromIncomplete(builderField.returning(factory.getLongFieldDataProvider()))

		fun returning(minimum: Double, maximum: Double): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.getDoubleFieldDataProvider(minimum, maximum)))
		}

		fun returning(minimum: Float, maximum: Float): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.getFloatFieldDataProvider(minimum, maximum)))
		}

		fun returning(minimum: Int, maximum: Int): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.getIntFieldDataProvider(minimum, maximum)))
		}

		fun returning(minimum: Long, maximum: Long): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.getLongFieldDataProvider(minimum, maximum)))
		}

		fun returningSequentialInteger(): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.sequentialIntegerFieldDataProvider))
		}

		fun returningSequentialInteger(startValue: Int): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.getSequentialIntegerFieldDataProvider(startValue)))
		}

		fun returningUuid(): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.uuidFieldDataProvider))
		}

		fun returningRgb(alpha: Boolean): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.getRgbFieldDataProvider(alpha)))
		}

		/**
		 * See [.returningLoremIpsum].
		 *
		 * @return builder generating one copy of the Lorem Ipsum text
		 */
		fun returningLoremIpsum(): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.loremIpsumFieldDataProvider))
		}

		/**
		 * See [.returningLoremIpsum].
		 *
		 * @param length The number of characters to return
		 * @return A builder generating a substring of an infinite (well, kind of) Lorem Ipsum text
		 */
		fun returningLoremIpsum(length: Int): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.getLoremIpsumFieldDataProvider(length)))
		}

		/**
		 * See [.returningLoremIpsum].
		 *
		 * @param minLength The minimal number of characters to return
		 * @param maxLength The maximal number of characters to return
		 * @return A builder generating a substring of an infinite (well, kind of) Lorem Ipsum text
		 */
		fun returningLoremIpsum(minLength: Int, maxLength: Int): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.getLoremIpsumFieldDataProvider(minLength, maxLength)))
		}

		/**
		 * Adds a String containing Lorem Ipsum. Length determines how many characters of Lorem Ipsum to return. The content will repeat itself if
		 * the requested length exceeds the length of Lorem Ipsum.
		 *
		 * @param minLength          The minimal number of characters to return
		 * @param maxLength          The maximal number of characters to return
		 * @param paragraphDelimiter The string to use between Lorem Ipsum paragraphs
		 * @return A builder generating a substring of an infinite (well, kind of) Lorem Ipsum text
		 */
		fun returningLoremIpsum(minLength: Int, maxLength: Int, paragraphDelimiter: String): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.getLoremIpsumFieldDataProvider(minLength, maxLength, paragraphDelimiter)))
		}

		/**
		 * Adds a generator of random enum values for the given field.
		 *
		 * @param enumClass  An enum class
		 * @param <ENUM_TYPE> Implicit. The enum type to use
		 * @return A builder with a data provider
		</ENUM_TYPE> */
		fun <ENUM_TYPE : Enum<*>> returning(enumClass: Class<ENUM_TYPE>): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.getRandomEnumFieldDataProvider(enumClass)))
		}

		/**
		 * Adds a [FieldDataProvider] generated value for the given field.
		 *
		 * @param fieldDataProvider An instance of the [FieldDataProvider] to use
		 * @param <VALUE_TYPE>       The type returned vy the [FieldDataProvider]
		 * @return An instance of the specified `VALUE_TYPE`
		</VALUE_TYPE> */
		fun <VALUE_TYPE> returning(fieldDataProvider: FieldDataProvider<RETURN_TYPE, VALUE_TYPE>): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(fieldDataProvider))
		}

		fun <VALUE_TYPE> returning(instances: Int, fieldDataProvider: FieldDataProvider<RETURN_TYPE, VALUE_TYPE>): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.getCustomListFieldDataProvider(instances, fieldDataProvider)))
		}

		fun <VALUE_TYPE> returning(minInstances: Int, maxInstances: Int,
		                           fieldDataProvider: FieldDataProvider<RETURN_TYPE, VALUE_TYPE>): BuilderField<RETURN_TYPE> {
			return getBuilderFieldFromIncomplete(builderField.returning(factory.getCustomListRangeFieldDataProvider(minInstances, maxInstances, fieldDataProvider)))
		}

		private fun getBuilderFieldFromIncomplete(incompleteBuilderField: IncompleteBuilderField<RETURN_TYPE>): BuilderField<RETURN_TYPE> {
			return if (incompleteBuilderField.initializeType == IncompleteBuilderField.InitializeType.WITH_CLASS) {
				BuilderField(incompleteBuilderField.generatedInstanceClass, incompleteBuilderField)

			} else {
				BuilderField(incompleteBuilderField.instanceProvider, incompleteBuilderField)
			}
		}
	}

	private inner class TypedArray<ELEMENT_TYPE> internal constructor(elementClass: Class<ELEMENT_TYPE>, capacity: Int) {
		private val typedArray: Array<ELEMENT_TYPE>

		init {
			// Use Array native method to create array of a type only known at run time
			val newInstance = java.lang.reflect.Array.newInstance(elementClass, capacity)

			typedArray = newInstance as Array<ELEMENT_TYPE>
		}

		internal fun get(): Array<ELEMENT_TYPE> {
			return typedArray
		}
	}

	/**
	 * Classes implementing this interface provide a new instance of the desired type that will get populated by [RandomGen].
	 *
	 * @param <INSTANCE_TYPE> The type of instance to be returned
	</INSTANCE_TYPE> */
	interface InstanceProvider<INSTANCE_TYPE> {
		fun provideInstance(): INSTANCE_TYPE
	}

	interface OnGenerateCallback<INSTANCE_TYPE> {
		fun onGenerate(pGeneratedInstance: INSTANCE_TYPE)
	}

	private class AssignmentException internal constructor(pException: Exception) : RuntimeException(pException)
}

