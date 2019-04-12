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
        given(random.nextDouble()).willReturn(0.0)

        // When
        var result = cut.invoke()

        // Then
        assertEquals(0, result.time)

        // Given
        given(random.nextDouble()).willReturn(0.999999999999999999)

        // When
        result = cut.invoke()

        // Then
        assertTrue((Long.MAX_VALUE - result.time).absoluteValue < 10L)
    }

    @Test
    fun givenZeroDoubleValueAndLatestTimestampWhenGenerateThenReturnsCorrectDate() {
        // Given
        cut = DateFieldDataProvider(random, latestTimestamp = 100L)
        given(random.nextDouble()).willReturn(0.0)

        // When
        val result = cut.invoke()

        // Then
        assertEquals(0, result.time)
    }

    @Test
    fun givenMaximalDoubleValueAndLatestTimestampWhenGenerateThenReturnsCorrectDate() {
        // Given
        cut = DateFieldDataProvider(random, latestTimestamp = 100L)
        given(random.nextDouble()).willReturn(0.9999999999999999)

        // When
        val result = cut.invoke()

        // Then
        assertEquals(100, result.time)
    }

    @Test
    fun givenZeroDoubleValueAndRangeWhenGenerateThenReturnsCorrectDate() {
        // Given
        cut = DateFieldDataProvider(random, 0L, 100L)
        given(random.nextDouble()).willReturn(0.0)

        // When
        val result = cut.invoke()

        // Then
        assertEquals(0, result.time)
    }

    @Test
    fun givenMaximalDoubleValueAndRangeWhenGenerateThenReturnsCorrectDate() {
        // Given
        cut = DateFieldDataProvider(random, 0L, 100L)
        given(random.nextDouble()).willReturn(0.9999999999999999)

        // When
        val result = cut.invoke()

        // Then
        assertEquals(100, result.time)
    }
}