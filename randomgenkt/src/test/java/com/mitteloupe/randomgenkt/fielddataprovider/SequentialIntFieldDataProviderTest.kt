package com.mitteloupe.randomgenkt.fielddataprovider

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Eran Boudjnah on 11/08/2018.
 */
class SequentialIntFieldDataProviderTest {
    private lateinit var cut: SequentialIntegerFieldDataProvider<Any>

    @Test
    fun givenNoInitialValueWhenGenerateThenReturnsCorrectSequence() {
        // Given
        cut = SequentialIntegerFieldDataProvider()

        // When
        var result = cut.invoke()

        // Then
        assertEquals(1, result)

        // When
        result = cut.invoke()

        // Then
        assertEquals(2, result)
    }

    @Test
    fun givenAnInitialValueWhenGenerateThenReturnsCorrectSequence() {
        // Given
        cut = SequentialIntegerFieldDataProvider(0xBED)

        // When
        var result = cut.invoke()

        // Then
        assertEquals(0xBED, result)

        // When
        result = cut.invoke()

        // Then
        assertEquals(0xBEE, result)
    }
}