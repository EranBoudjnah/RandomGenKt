package com.mitteloupe.randomgenkt.fielddataprovider

import com.nhaarman.mockitokotlin2.given
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
class DoubleFieldDataProviderTest {
    private lateinit var cut: DoubleFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Test
    fun givenRandomDoubleValueWhenGenerateThenReturnsSameValue() {
        // Given
        cut = DoubleFieldDataProvider.getInstance(random)
        given(random.nextDouble()).willReturn(0.0)

        // When
        var result = cut.invoke()

        // Then
        assertEquals(0.0, result, 0.00001)

        // Given
        given(random.nextDouble()).willReturn(0.99999999999)

        // When
        result = cut.invoke()

        // Then
        assertEquals(1.0, result, 0.00001)
    }

    @Test
    fun givenRandomDoubleValueAndRangeWhenGenerateThenReturnsCorrectValue() {
        // Given
        cut = DoubleFieldDataProvider.getInstanceWithRange(random, Double.MIN_VALUE, Double.MAX_VALUE)
        given(random.nextDouble()).willReturn(0.0)

        // When
        var result = cut.invoke()

        // Then
        assertEquals(Double.MIN_VALUE, result, 0.00001)

        // Given
        given(random.nextDouble()).willReturn(0.999999999999999999)

        // When
        result = cut.invoke()

        // Then
        assertEquals(Double.MAX_VALUE, result, 0.00001)
    }
}