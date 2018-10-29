package com.mitteloupe.randomgen

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultValuesInstanceProviderTest {
	private lateinit var cut: DefaultValuesInstanceProvider<*>

	@Test
	fun `Given constructor with parameters when provideInstance then returns instance with default values`() {
		// Given
		cut = DefaultValuesInstanceProvider(TestClassWithFields::class.java)

		// When
		val result = cut.provideInstance() as TestClassWithFields

		// Then
		assertEquals(0, result.byteField.toLong())
		assertEquals(0, result.shortField.toLong())
		assertEquals(0, result.intField.toLong())
		assertEquals(0, result.longField)
		assertEquals(0f, result.floatField, 0f)
		assertEquals(0.0, result.doubleField, 0.0)
		assertFalse(result.isBooleanField)
		assertNull(result.stringField)
		assertNull(result.`object`)
		assertNull(result.objects)
	}

	@Test
	fun `Given no defined constructor when provideInstance then returns instance`() {
		// Given
		cut = DefaultValuesInstanceProvider(TestClassWithNoConstructor::class.java)

		// When
		val result = cut.provideInstance() as TestClassWithNoConstructor

		// Then
		assertNotNull(result)
	}

	@Test
	fun `Given public constructor with no parameters when provide instance then uses constructor`() {
		// Given
		cut = DefaultValuesInstanceProvider(TestClassWithNoFields::class.java)

		// When
		val result = cut.provideInstance() as TestClassWithNoFields

		// Then
		assertTrue(result.isSet)
	}

	private interface TestClass

	private class TestClassWithNoConstructor : TestClass

	private class TestClassWithNoFields : TestClass {
		internal val isSet: Boolean = true
	}

	private class TestClassWithFields(internal val byteField: Byte, internal val shortField: Short, internal val intField: Int,
	                                  internal val longField: Long, internal val floatField: Float, internal val doubleField: Double,
	                                  internal val isBooleanField: Boolean, internal val stringField: String, internal val `object`: Any,
	                                  val objects: List<Any>) : TestClass
}