package com.mitteloupe.randomgenkt

import com.mitteloupe.randomgenkt.model.DataProviderMap
import com.mitteloupe.randomgenkt.model.InstanceProvider
import java.lang.reflect.Constructor

internal class JavaDefaultValuesInstanceProvider<GENERATED_INSTANCE : Any>(
    private val generatedInstanceClass: Class<GENERATED_INSTANCE>
) : InstanceProvider<GENERATED_INSTANCE> {
    private var validConstructor: Constructor<GENERATED_INSTANCE>? = null

    private val allPublicConstructors: MutableList<Constructor<GENERATED_INSTANCE>>

    init {
        @Suppress("UNCHECKED_CAST")
        val typedConstructors =
            generatedInstanceClass.constructors as Array<Constructor<GENERATED_INSTANCE>>
        allPublicConstructors = typedConstructors.toMutableList()
    }

    override fun invoke(dataProviders: DataProviderMap<GENERATED_INSTANCE>) = try {
        instanceFromAnyConstructor()
    } catch (exception: Exception) {
        throw InstanceCreationException(
            "Failed to instantiate ${generatedInstanceClass.simpleName}. " +
                "Try providing a ValuesInstanceProvider.",
            exception
        )
    }

    @Throws(Exception::class)
    private fun instanceFromAnyConstructor(): GENERATED_INSTANCE {
        validConstructor?.let {
            return getInstance(validConstructor)
        }

        do {
            val constructorToUse = allPublicConstructors.firstOrNull()
            try {
                val instance = getInstance(constructorToUse)
                validConstructor = constructorToUse
                return instance
            } catch (throwable: Throwable) {
                constructorToUse?.let {
                    allPublicConstructors.remove(it)
                }
            }
        } while (allPublicConstructors.isNotEmpty())

        return generateInstanceWithNewConstructor()
    }

    @Throws(Exception::class)
    private fun getInstance(constructorToUse: Constructor<GENERATED_INSTANCE>?) =
        if (constructorToUse == null) {
            generateInstanceWithNewConstructor()
        } else {
            val parameterTypes = constructorToUse.parameterTypes
            val parameterValues = getParameterValues(parameterTypes)

            generatedInstanceWithParameters(constructorToUse, *parameterValues)
        }

    private fun getParameterValues(parameters: Array<Class<*>>) = parameters.map { parameter ->
        when (parameter) {
            Short::class.javaPrimitiveType, Short::class.javaObjectType -> 0.toShort()
            Int::class.javaPrimitiveType, Int::class.javaObjectType -> 0
            Long::class.javaPrimitiveType, Long::class.javaObjectType -> 0L
            Float::class.javaPrimitiveType, Float::class.javaObjectType -> 0f
            Double::class.javaPrimitiveType, Double::class.javaObjectType -> 0.0
            Byte::class.javaPrimitiveType, Byte::class.javaObjectType -> '\u0000'.code.toByte()
            Boolean::class.javaPrimitiveType, Boolean::class.javaObjectType -> false
            String::class.javaPrimitiveType, String::class.javaObjectType -> ""
            Any::class.javaObjectType -> Any()
            List::class.javaObjectType -> emptyList<Any>()
            Array::class.javaObjectType -> emptyArray<Any>()
            Map::class.javaObjectType -> emptyMap<Any, Any>()
            Set::class.javaObjectType -> emptySet<Any>()
            else -> null
        }
    }.toTypedArray()

    @Throws(Exception::class)
    private fun generatedInstanceWithParameters(
        constructor: Constructor<GENERATED_INSTANCE>,
        vararg parameterValues: Any?
    ): GENERATED_INSTANCE {
        constructor.isAccessible = true

        return constructor.newInstance(*parameterValues)
    }

    @Throws(Exception::class)
    private fun generateInstanceWithNewConstructor() =
        generateInstanceWithNewConstructorUsingReflectionFactory()

    @Throws(Exception::class)
    private fun generateInstanceWithNewConstructorUsingReflectionFactory(): GENERATED_INSTANCE =
        generatedInstanceClass.constructors.firstNotNullOf { constructor ->
            constructor.newInstance()
        } as GENERATED_INSTANCE

    class InstanceCreationException(message: String, exception: Exception) :
        RuntimeException(message, exception)
}
