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
class IntFieldDataProviderTest {
    private lateinit var cut: IntFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Test
    fun givenRandomDoubleValueWhenGenerateThenReturnsIntegerValue() {
        // Given
        cut = IntFieldDataProvider(random)
        given(random.nextDouble()).willReturn(0.0)

        // When
        var result = cut.invoke()

        // Then
        assertEquals(Int.MIN_VALUE, result)

        // Given
        given(random.nextDouble()).willReturn(0.99999999999)

        // When
        result = cut.invoke()

        // Then
        assertEquals(Int.MAX_VALUE, result)
    }

    @Test
    fun givenRandomFloatValueAndRangeWhenGenerateThenReturnsCorrectValue() {
        // Given
        cut = IntFieldDataProvider(random, 0, 100)
        given(random.nextDouble()).willReturn(0.0)

        // When
        var result = cut.invoke()

        // Then
        assertEquals(0, result)

        // Given
        given(random.nextDouble()).willReturn(0.99999999999)

        // When
        result = cut.invoke()

        // Then
        assertEquals(100, result)
    }
}