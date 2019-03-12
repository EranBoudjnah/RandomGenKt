package com.mitteloupe.randomgenkt.fielddataprovider

import com.nhaarman.mockitokotlin2.given
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.Random
import kotlin.math.absoluteValue

/**
 * Created by Eran Boudjnah on 10/08/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LongFieldDataProviderTest {
	private lateinit var cut: LongFieldDataProvider<Any>

	@Mock
	private lateinit var random: Random

	@Test
	fun givenRandomDoubleValueWhenGenerateThenReturnsLongValue() {
		// Given
		cut = LongFieldDataProvider(random)
		given(random.nextDouble()).willReturn(0.0)

		// When
		var result = cut.invoke()

		// Then
		assertEquals(Long.MIN_VALUE, result)

		// Given
		given(random.nextDouble()).willReturn(0.99999999999999999999999)

		// When
		result = cut.invoke()

		// Then
		assertTrue((Long.MAX_VALUE - result).absoluteValue < 10L)
	}

	@Test
	fun givenRandomFloatValueAndRangeWhenGenerateThenReturnsCorrectValue() {
		// Given
		cut = LongFieldDataProvider(random, 0L, 100L)
		given(random.nextDouble()).willReturn(0.0)

		// When
		var result = cut.invoke()

		// Then
		assertEquals(0, result)

		// Given
		given(random.nextDouble()).willReturn(0.9999999999999999)

		// When
		result = cut.invoke()

		// Then
		assertEquals(100, result)
	}
}