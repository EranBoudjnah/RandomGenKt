package com.mitteloupe.randomgenkt.fielddataprovider

import java.util.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DoubleFieldDataProviderTest {
    private lateinit var classUnderTest: DoubleFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Test
    fun givenRandomDoubleValueWhenGenerateThenReturnsSameValue() {
        // Given
        classUnderTest = DoubleFieldDataProvider(random)
        given(random.nextDouble()).willReturn(0.0)

        // When
        var result = classUnderTest.invoke()

        // Then
        assertEquals(0.0, result, 0.00001)

        // Given
        given(random.nextDouble()).willReturn(0.99999999999)

        // When
        result = classUnderTest.invoke()

        // Then
        assertEquals(1.0, result, 0.00001)
    }

    @Test
    fun givenRandomDoubleValueAndRangeWhenGenerateThenReturnsCorrectValue() {
        // Given
        classUnderTest = DoubleFieldDataProvider(
            random,
            minimum = Double.MIN_VALUE,
            maximum = Double.MAX_VALUE
        )
        given(random.nextDouble()).willReturn(0.0)

        // When
        var result = classUnderTest.invoke()

        // Then
        assertEquals(Double.MIN_VALUE, result, 0.00001)

        // Given
        given(random.nextDouble()).willReturn(0.999999999999999999)

        // When
        result = classUnderTest.invoke()

        // Then
        assertEquals(Double.MAX_VALUE, result, 0.00001)
    }
}
