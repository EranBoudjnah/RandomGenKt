package com.mitteloupe.randomgenkt.fielddataprovider

import java.util.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ShortFieldDataProviderTest {
    private lateinit var classUnderTest: ShortFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Test
    fun givenRandomDoubleValueWhenGenerateThenReturnsIntegerValue() {
        // Given
        classUnderTest = ShortFieldDataProvider(random)
        given(random.nextDouble()).willReturn(0.0)

        // When
        var result = classUnderTest.invoke()

        // Then
        assertEquals(Short.MIN_VALUE, result)

        // Given
        given(random.nextDouble()).willReturn(0.99999999999)

        // When
        result = classUnderTest.invoke()

        // Then
        assertEquals(Short.MAX_VALUE, result)
    }

    @Test
    fun givenRandomFloatValueAndRangeWhenGenerateThenReturnsCorrectValue() {
        // Given
        classUnderTest = ShortFieldDataProvider(random, 0, 100)
        given(random.nextDouble()).willReturn(0.0)

        // When
        var result = classUnderTest.invoke()

        // Then
        assertEquals(0.toShort(), result)

        // Given
        given(random.nextDouble()).willReturn(0.99999999999)

        // When
        result = classUnderTest.invoke()

        // Then
        assertEquals(100.toShort(), result)
    }
}
