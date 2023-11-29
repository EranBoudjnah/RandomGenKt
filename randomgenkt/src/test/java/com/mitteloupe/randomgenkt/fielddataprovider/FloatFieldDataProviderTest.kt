package com.mitteloupe.randomgenkt.fielddataprovider

import java.util.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FloatFieldDataProviderTest {
    private lateinit var classUnderTest: FloatFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Test
    fun `Given random float value when generate then returns same value`() {
        // Given
        classUnderTest = FloatFieldDataProvider(random)
        given(random.nextFloat()).willReturn(0f)

        // When
        var result = classUnderTest.invoke()

        // Then
        assertEquals(0.0f, result, 0.00001f)

        // Given
        given(random.nextFloat()).willReturn(1f)

        // When
        result = classUnderTest.invoke()

        // Then
        assertEquals(1.0f, result, 0.00001f)
    }

    @Test
    fun `Given random float value and range when generate then returns correct value`() {
        // Given
        classUnderTest = FloatFieldDataProvider(random, Float.MIN_VALUE, Float.MAX_VALUE)
        given(random.nextFloat()).willReturn(0f)

        // When
        var result = classUnderTest.invoke()

        // Then
        assertEquals(Float.MIN_VALUE, result, 0.00001f)

        // Given
        given(random.nextFloat()).willReturn(1f)

        // When
        result = classUnderTest.invoke()

        // Then
        assertEquals(Float.MAX_VALUE, result, 0.00001f)
    }
}
