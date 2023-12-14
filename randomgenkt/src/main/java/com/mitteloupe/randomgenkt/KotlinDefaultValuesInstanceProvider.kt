package com.mitteloupe.randomgenkt

import com.mitteloupe.randomgenkt.model.DataProviderMap
import com.mitteloupe.randomgenkt.model.InstanceProvider
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.isAccessible

internal class KotlinDefaultValuesInstanceProvider<GENERATED_INSTANCE : Any>(
    private val generatedInstanceClass: KClass<GENERATED_INSTANCE>
) : InstanceProvider<GENERATED_INSTANCE> {
    private var validConstructor: KFunction<GENERATED_INSTANCE>? = null

    private val allPublicConstructors: List<KFunction<GENERATED_INSTANCE>>

    init {
        val typedConstructors = generatedInstanceClass.constructors
        allPublicConstructors = typedConstructors.toList()
    }

    override fun invoke(dataProviders: DataProviderMap<GENERATED_INSTANCE>) = try {
        instanceFromAnyConstructor(dataProviders)
    } catch (exception: Exception) {
        throw InstanceCreationException(
            "Failed to instantiate ${generatedInstanceClass.simpleName}. " +
                "Try providing a ValuesInstanceProvider.",
            exception
        )
    }

    @Throws(Exception::class)
    private fun instanceFromAnyConstructor(
        dataProviders: DataProviderMap<GENERATED_INSTANCE>
    ): GENERATED_INSTANCE {
        validConstructor?.let { validConstructor ->
            return generatedInstance(validConstructor, dataProviders)
        }

        return allPublicConstructors.firstNotNullOfOrNull { constructorToUse ->
            try {
                val instance = generatedInstance(constructorToUse, dataProviders)
                validConstructor = constructorToUse
                instance
            } catch (ignore: Throwable) {
                null
            }
        } ?: throw RuntimeException(
            "java.lang.RuntimeException: No usable public constructors found."
        )
    }

    private fun generatedInstance(
        constructor: KFunction<GENERATED_INSTANCE>,
        dataProviders: DataProviderMap<GENERATED_INSTANCE>
    ): GENERATED_INSTANCE {
        val parameterValues = getParameterValues(constructor.parameters, dataProviders)

        constructor.isAccessible = true

        return constructor.call(*parameterValues)
    }

    private fun getParameterValues(
        parameters: List<KParameter>,
        dataProviders: DataProviderMap<GENERATED_INSTANCE>
    ) = parameters.map { parameter ->
        dataProviders[parameter.name]?.run {
            if (declarationSite.constructor) {
                return@map fieldDataProvider(null)
            }
        }

        if (parameter.type.isMarkedNullable) {
            null
        } else {
            val parameterTypeClassifier = parameter.type.classifier
            if (parameterTypeClassifier is KClass<*> && parameterTypeClassifier.java.isEnum) {
                val result = parameterTypeClassifier.java.enumConstants.firstOrNull()
                result
            } else {
                val result = parameterTypeClassifier.primitiveParameterValue
                result
            }
        }
    }.toTypedArray()

    private val KClassifier?.primitiveParameterValue: Any?
        get() = when (this) {
            Short::class -> 0.toShort()
            Int::class -> 0
            Long::class -> 0L
            Float::class -> 0f
            Double::class -> 0.0
            Byte::class -> '\u0000'.code.toByte()
            Boolean::class -> false
            String::class -> ""
            Any::class -> Any()
            List::class -> emptyList<Any>()
            Array::class -> emptyArray<Any>()
            Map::class -> emptyMap<Any, Any>()
            Set::class -> emptySet<Any>()
            else -> null
        }

    class InstanceCreationException(message: String, exception: Exception) :
        RuntimeException(message, exception)
}
