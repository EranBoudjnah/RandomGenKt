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
class FloatFieldDataProviderTest {
    private lateinit var cut: FloatFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Test
    fun `Given random float value when generate then returns same value`() {
        // Given
        cut = FloatFieldDataProvider(random)
        given(random.nextFloat()).willReturn(0f)

        // When
        var result = cut.invoke()

        // Then
        assertEquals(0.0f, result, 0.00001f)

        // Given
        given(random.nextFloat()).willReturn(1f)

        // When
        result = cut.invoke()

        // Then
        assertEquals(1.0f, result, 0.00001f)
    }

    @Test
    fun `Given random float value and range when generate then returns correct value`() {
        // Given
        cut = FloatFieldDataProvider(random, Float.MIN_VALUE, Float.MAX_VALUE)
        given(random.nextFloat()).willReturn(0f)

        // When
        var result = cut.invoke()

        // Then
        assertEquals(Float.MIN_VALUE, result, 0.00001f)

        // Given
        given(random.nextFloat()).willReturn(1f)

        // When
        result = cut.invoke()

        // Then
        assertEquals(Float.MAX_VALUE, result, 0.00001f)
    }
}