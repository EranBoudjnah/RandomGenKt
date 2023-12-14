package com.mitteloupe.randomgenkt.fielddataprovider

import java.util.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LongFieldDataProviderTest {
    private lateinit var classUnderTest: LongFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Test
    fun `Given minimum random when invoked then returns minimal value`() {
        // Given
        classUnderTest = LongFieldDataProvider(random)
        given(random.nextDouble()).willReturn(0.0)

        // When
        val actualResult = classUnderTest()

        // Then
        assertEquals(Long.MIN_VALUE, actualResult)
    }

    @Test
    fun `Given maximum random when invoked then returns maximal value`() {
        // Given
        classUnderTest = LongFieldDataProvider(random)
        given(random.nextDouble()).willReturn(0.9999999999999999)

        // When
        val actualResult = classUnderTest()

        // Then
        assertEquals(Long.MAX_VALUE, actualResult)
    }

    @Test
    fun `Given range, minimum random when invoked then returns minimal value`() {
        // Given
        val minimum = 0L
        classUnderTest = LongFieldDataProvider(random, minimum, 100L)
        given(random.nextDouble()).willReturn(0.0)

        // When
        val actualResult = classUnderTest()

        // Then
        assertEquals(minimum, actualResult)
    }

    @Test
    fun `Given range, maximum random when invoked then returns maximal value`() {
        // Given
        val maximum = 100L
        classUnderTest = LongFieldDataProvider(random, 0L, maximum)
        given(random.nextDouble()).willReturn(0.9999999999999999)

        // When
        val actualResult = classUnderTest()

        // Then
        assertEquals(maximum, actualResult)
    }
}
