package com.mitteloupe.randomgenkt.fielddataprovider

import org.junit.Assert.assertEquals
import org.junit.Test

class SequentialIntFieldDataProviderTest {
    private lateinit var classUnderTest: SequentialIntegerFieldDataProvider<Any>

    @Test
    fun givenNoInitialValueWhenGenerateThenReturnsCorrectSequence() {
        // Given
        classUnderTest = SequentialIntegerFieldDataProvider()

        // When
        var result = classUnderTest.invoke()

        // Then
        assertEquals(1, result)

        // When
        result = classUnderTest.invoke()

        // Then
        assertEquals(2, result)
    }

    @Test
    fun givenAnInitialValueWhenGenerateThenReturnsCorrectSequence() {
        // Given
        classUnderTest = SequentialIntegerFieldDataProvider(0xBED)

        // When
        var result = classUnderTest.invoke()

        // Then
        assertEquals(0xBED, result)

        // When
        result = classUnderTest.invoke()

        // Then
        assertEquals(0xBEE, result)
    }
}
