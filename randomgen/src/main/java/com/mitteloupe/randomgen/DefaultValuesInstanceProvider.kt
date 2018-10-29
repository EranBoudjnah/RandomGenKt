package com.mitteloupe.randomgen

import sun.misc.Unsafe
import sun.reflect.ReflectionFactory
import java.lang.reflect.Constructor

internal class DefaultValuesInstanceProvider<GENERATED_INSTANCE>(
	private val generatedInstanceClass: Class<GENERATED_INSTANCE>
) : () -> GENERATED_INSTANCE {
	private var validConstructor: Constructor<GENERATED_INSTANCE>? = null

	private val allPublicConstructors: MutableList<Constructor<GENERATED_INSTANCE>>

	init {
		@Suppress("UNCHECKED_CAST")
		val typedConstructors = generatedInstanceClass.constructors as Array<Constructor<GENERATED_INSTANCE>>
		allPublicConstructors = typedConstructors.toMutableList()
	}

	override fun invoke(): GENERATED_INSTANCE {
		try {
			return instanceFromAnyConstructor()

		} catch (exception: Exception) {
			throw InstanceCreationException(
				"Failed to instantiate ${generatedInstanceClass.simpleName}. Try providing a ValuesInstanceProvider.",
				exception
			)
		}
	}

	@Throws(Exception::class)
	private fun instanceFromAnyConstructor(): GENERATED_INSTANCE {
		validConstructor?.let {
			return getInstance(validConstructor)
		}

		do {
			val constructorToUse = getPreferredConstructor(allPublicConstructors)
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
	private fun getInstance(constructorToUse: Constructor<GENERATED_INSTANCE>?): GENERATED_INSTANCE =
		if (constructorToUse == null) {
			generateInstanceWithNewConstructor()

		} else {
			val parameterTypes = constructorToUse.parameterTypes
			val parameterValues = getParameterValues(parameterTypes)

			generatedInstanceWithParameters(constructorToUse, *parameterValues)
		}

	private fun getPreferredConstructor(pConstructors: List<Constructor<GENERATED_INSTANCE>>) =
		pConstructors.firstOrNull()

	private fun getParameterValues(pParameters: Array<Class<*>>) = pParameters
		.map {
			@Suppress("IMPLICIT_CAST_TO_ANY")
			when (it) {
				Short::class.javaPrimitiveType -> 0.toShort()
				Int::class.javaPrimitiveType -> 0
				Long::class.javaPrimitiveType -> 0L
				Float::class.javaPrimitiveType -> 0f
				Double::class.javaPrimitiveType -> 0.0
				Byte::class.javaPrimitiveType -> '\u0000'.toByte()
				Boolean::class.javaPrimitiveType -> false
				else -> null
			}
		}
		.toTypedArray()

	@Throws(Exception::class)
	private fun generatedInstanceWithParameters(constructor: Constructor<GENERATED_INSTANCE>, vararg parameterValues: Any?): GENERATED_INSTANCE {
		constructor.isAccessible = true

		return constructor.newInstance(*parameterValues)
	}

	@Throws(Exception::class)
	private fun generateInstanceWithNewConstructor() =
		try {
			generateInstanceWithNewConstructorUsingReflectionFactory()

		} catch (throwable: Throwable) {
			generateInstanceWithNewConstructorUsingUnsafe()
		}

	@Throws(Exception::class)
	private fun generateInstanceWithNewConstructorUsingReflectionFactory(): GENERATED_INSTANCE {
		val reflection = ReflectionFactory.getReflectionFactory()

		@Suppress("UNCHECKED_CAST")
		val constructor = reflection.newConstructorForSerialization(
			generatedInstanceClass, Any::class.java.getDeclaredConstructor()
		) as Constructor<GENERATED_INSTANCE>
		return constructor.newInstance()
	}

	@Throws(Exception::class)
	private fun generateInstanceWithNewConstructorUsingUnsafe(): GENERATED_INSTANCE {
		val unsafeConstructor = Unsafe::class.java.getDeclaredConstructor()
		unsafeConstructor.isAccessible = true
		val unsafe = unsafeConstructor.newInstance()

		@Suppress("UNCHECKED_CAST")
		return unsafe.allocateInstance(generatedInstanceClass) as GENERATED_INSTANCE
	}

	private class InstanceCreationException(pMessage: String, pException: Exception) : RuntimeException(pMessage, pException)
}
