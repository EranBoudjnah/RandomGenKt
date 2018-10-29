package com.mitteloupe.randomgen.fielddataprovider

import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.Random

/**
 * Created by Eran Boudjnah on 10/08/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class IntFieldDataProviderTest {
	private lateinit var cut: IntFieldDataProvider<Any>

	@Mock
	private lateinit var random: Random

	@Test
	fun givenRandomDoubleValueWhenGenerateThenReturnsIntegerValue() {
		// Given
		cut = IntFieldDataProvider(random)
		whenever(random.nextDouble()).thenReturn(0.0)

		// When
		var result = cut.generate()

		// Then
		assertEquals(Int.MIN_VALUE, result)

		// Given
		whenever(random.nextDouble()).thenReturn(0.99999999999)

		// When
		result = cut.generate()

		// Then
		assertEquals(Int.MAX_VALUE, result)
	}

	@Test
	fun givenRandomFloatValueAndRangeWhenGenerateThenReturnsCorrectValue() {
		// Given
		cut = IntFieldDataProvider(random, 0, 100)
		whenever(random.nextDouble()).thenReturn(0.0)

		// When
		var result = cut.generate()

		// Then
		assertEquals(0, result)

		// Given
		whenever(random.nextDouble()).thenReturn(0.99999999999)

		// When
		result = cut.generate()

		// Then
		assertEquals(100, result)
	}
}