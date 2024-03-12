package com.mitteloupe.randomgenkt.fielddataprovider

import java.util.Random
import kotlin.math.absoluteValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.lessThan
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DateFieldDataProviderTest {
    private lateinit var classUnderTest: DateFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Test
    fun givenRandomDoubleValueWhenGenerateThenReturnsCorrectDate() {
        // Given
        classUnderTest = DateFieldDataProvider(random)
        given(random.nextDouble()).willReturn(0.0)

        // When
        var result = classUnderTest.invoke()

        // Then
        assertEquals(0, result.time)

        // Given
        given(random.nextDouble()).willReturn(0.9999999999999999)

        // When
        result = classUnderTest.invoke()

        // Then
        assertThat((Long.MAX_VALUE - result.time).absoluteValue, `is`(lessThan(1000L)))
    }

    @Test
    fun givenZeroDoubleValueAndLatestTimestampWhenGenerateThenReturnsCorrectDate() {
        // Given
        classUnderTest = DateFieldDataProvider(random, latestTimestamp = 100L)
        given(random.nextDouble()).willReturn(0.0)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertEquals(0, result.time)
    }

    @Test
    fun givenMaximalDoubleValueAndLatestTimestampWhenGenerateThenReturnsCorrectDate() {
        // Given
        classUnderTest = DateFieldDataProvider(random, latestTimestamp = 100L)
        given(random.nextDouble()).willReturn(0.9999999999999999)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertEquals(100, result.time)
    }

    @Test
    fun givenZeroDoubleValueAndRangeWhenGenerateThenReturnsCorrectDate() {
        // Given
        classUnderTest = DateFieldDataProvider(random, 0L, 100L)
        given(random.nextDouble()).willReturn(0.0)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertEquals(0, result.time)
    }

    @Test
    fun givenMaximalDoubleValueAndRangeWhenGenerateThenReturnsCorrectDate() {
        // Given
        classUnderTest = DateFieldDataProvider(random, 0L, 100L)
        given(random.nextDouble()).willReturn(0.9999999999999999)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertEquals(100, result.time)
    }
}
