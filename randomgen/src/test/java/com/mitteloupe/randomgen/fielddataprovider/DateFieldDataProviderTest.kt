package com.mitteloupe.randomgen.fielddataprovider

import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.Random
import kotlin.math.absoluteValue

/**
 * Created by Eran Boudjnah on 23/09/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class DateFieldDataProviderTest {
	private lateinit var cut: DateFieldDataProvider<Any>

	@Mock
	private lateinit var random: Random

	@Test
	fun givenRandomDoubleValueWhenGenerateThenReturnsCorrectDate() {
		// Given
		cut = DateFieldDataProvider(random)
		whenever(random.nextDouble()).thenReturn(0.0)

		// When
		var result = cut.generate()

		// Then
		assertEquals(0, result.time)

		// Given
		whenever(random.nextDouble()).thenReturn(0.999999999999999999)

		// When
		result = cut.generate()

		// Then
		assertTrue((Long.MAX_VALUE - result.time).absoluteValue < 10L)
	}

	@Test
	fun givenZeroDoubleValueAndLatestTimestampWhenGenerateThenReturnsCorrectDate() {
		// Given
		cut = DateFieldDataProvider(random, latestTimestamp = 100L)
		whenever(random.nextDouble()).thenReturn(0.0)

		// When
		val result = cut.generate()

		// Then
		assertEquals(0, result.time)
	}

	@Test
	fun givenMaximalDoubleValueAndLatestTimestampWhenGenerateThenReturnsCorrectDate() {
		// Given
		cut = DateFieldDataProvider(random, latestTimestamp = 100L)
		whenever(random.nextDouble()).thenReturn(0.9999999999999999)

		// When
		val result = cut.generate()

		// Then
		assertEquals(100, result.time)
	}

	@Test
	fun givenZeroDoubleValueAndRangeWhenGenerateThenReturnsCorrectDate() {
		// Given
		cut = DateFieldDataProvider(random, 0L, 100L)
		whenever(random.nextDouble()).thenReturn(0.0)

		// When
		val result = cut.generate()

		// Then
		assertEquals(0, result.time)
	}

	@Test
	fun givenMaximalDoubleValueAndRangeWhenGenerateThenReturnsCorrectDate() {
		// Given
		cut = DateFieldDataProvider(random, 0L, 100L)
		whenever(random.nextDouble()).thenReturn(0.9999999999999999)

		// When
		val result = cut.generate()

		// Then
		assertEquals(100, result.time)
	}
}