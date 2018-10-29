package com.mitteloupe.randomgen.fielddataprovider

import com.mitteloupe.randomgen.FieldDataProvider
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.Random

private const val FIRST_VALUE = "Superman"
private const val SECOND_VALUE = "Spider Man"
private const val THIRD_VALUE = "Batman"

/**
 * Created by Eran Boudjnah on 29/09/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class WeightedFieldDataProvidersFieldDataProviderTest {
	private lateinit var cut: WeightedFieldDataProvidersFieldDataProvider<TestClass, String>

	@Mock
	private lateinit var random: Random
	@Mock
	private lateinit var fieldDataProvider: FieldDataProvider<TestClass, String>

	@Before
	fun setUp() {
		whenever(fieldDataProvider.generate(any(TestClass::class.java))).thenReturn(FIRST_VALUE)

		cut = WeightedFieldDataProvidersFieldDataProvider(random, fieldDataProvider)
	}

	@Test
	fun givenAnyInstanceWhenGenerateThenReturnsFirstValue() {
		// Given
		val instance = TestClass()

		// When
		val result = cut.generate(instance)

		// Then
		assertEquals(FIRST_VALUE, result)
	}

	@Test
	fun givenASecondFieldDataProviderAndAnyInstanceWhenGenerateThenReturnsExpectedValue() {
		// Given
		val instance = TestClass()
		val fieldDataProvider2 = mock<FieldDataProvider<TestClass, String>>()
		whenever(fieldDataProvider2.generate(instance)).thenReturn(SECOND_VALUE)
		cut.addFieldDataProvider(fieldDataProvider2, 2.0)
		val fieldDataProvider3 = mock<FieldDataProvider<TestClass, String>>()
		whenever(fieldDataProvider3.generate(instance)).thenReturn(THIRD_VALUE)
		cut.addFieldDataProvider(fieldDataProvider3, 3.0)
		whenever(random.nextDouble()).thenReturn(0.7)

		// When
		var result = cut.generate(instance)

		// Then
		assertEquals(THIRD_VALUE, result)

		// Given
		whenever(random.nextDouble()).thenReturn(0.3)

		// When
		result = cut.generate(instance)

		// Then
		assertEquals(SECOND_VALUE, result)

		// Given
		whenever(random.nextDouble()).thenReturn(0.1)

		// When
		result = cut.generate(instance)

		// Then
		assertEquals(FIRST_VALUE, result)
	}

	private class TestClass
}