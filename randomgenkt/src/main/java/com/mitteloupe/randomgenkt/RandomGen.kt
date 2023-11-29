package com.mitteloupe.randomgenkt

import com.mitteloupe.randomgenkt.model.DataProviderMap
import com.mitteloupe.randomgenkt.model.InstanceProvider
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

class RandomGen<GENERATED_INSTANCE : Any> internal constructor(
    private val instanceProvider: InstanceProvider<GENERATED_INSTANCE>,
    private val dataProviders: DataProviderMap<GENERATED_INSTANCE>,
    private val onGenerateCallbacks: List<OnGenerateCallback<GENERATED_INSTANCE>>
) : FieldDataProvider<Any?, GENERATED_INSTANCE>() {
    private val fields by lazy { getAllFields(dataProviders) }

    override fun invoke(instance: Any?): GENERATED_INSTANCE {
        val providedInstance = instanceProvider(dataProviders)

        providedInstance.validateAllProvidedFields()

        providedInstance.setAllFields()

        notifyOnGenerateCallbacks(providedInstance)

        return providedInstance
    }

    private val GENERATED_INSTANCE.constructorParameterNames: List<String>
        get() = this::class.constructors.flatMap { constructor ->
            constructor.parameters.mapNotNull { parameter -> parameter.name }
        } + this::class.java.constructors.flatMap { constructor ->
            constructor.parameters.map { parameter -> parameter.name }
        }

    private fun GENERATED_INSTANCE.validateAllProvidedFields() {
        val unusedDataProviders = dataProviders.map { (fieldName, _) -> fieldName }.toMutableSet()
        val allFields = constructorParameterNames + fields.keys
        allFields.forEach(unusedDataProviders::remove)
        if (unusedDataProviders.isNotEmpty()) {
            throw IllegalArgumentException(
                "Field(s) not found: ${unusedDataProviders.joinToString(", ")}"
            )
        }
    }

    private fun GENERATED_INSTANCE.setAllFields() {
        dataProviders.forEach { (fieldName, configuredFieldDataProvider) ->
            val setField = configuredFieldDataProvider.declarationSite.classBody
            if (setField && fields.containsKey(fieldName)) {
                try {
                    fields[fieldName]?.let { field ->
                        val generatedValue = configuredFieldDataProvider.fieldDataProvider(this)
                        setField(this, field, generatedValue)
                    }
                } catch (exception: AssignmentException) {
                    throw IllegalArgumentException(
                        "Cannot set field $fieldName due to an invalid value",
                        exception
                    )
                }
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
        if (value is ByteArray) {
            return value
        }

        if (!isCollection(value)) {
            throw AssignmentException(RuntimeException("Expected collection value"))
        }

        val valueAsCollection = ArrayList(value as Collection<Any?>)

        try {
            val genericType = field.type.componentType as Class<*>
            return valueAsCollection.toArray(TypedArray(genericType, valueAsCollection.size).get())
        } catch (exception: ArrayStoreException) {
            throw AssignmentException(exception)
        } catch (exception: ClassCastException) {
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

    private fun getAllFields(
        dataProviders: DataProviderMap<GENERATED_INSTANCE>
    ): Map<String, Field> {
        val fields: MutableMap<String, Field> = HashMap()
        val instance = instanceProvider(dataProviders)
        val allProperties = instance::class.declaredMemberProperties
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

    private inner class TypedArray<ELEMENT_TYPE : Any>(
        elementClass: Class<ELEMENT_TYPE>,
        capacity: Int
    ) {
        private val typedArray: Array<ELEMENT_TYPE>

        init {
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
