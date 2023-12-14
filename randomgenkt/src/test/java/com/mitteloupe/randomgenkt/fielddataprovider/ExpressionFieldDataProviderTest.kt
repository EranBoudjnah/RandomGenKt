package com.mitteloupe.randomgenkt.fielddataprovider

import org.junit.Assert.assertEquals
import org.junit.Test

class ExpressionFieldDataProviderTest {
    @Test
    fun `Given String to Int lambda, String when invoked then returns the string as an integer`() {
        // Given
        val classUnderTest = ExpressionFieldDataProvider<String, Int> { input -> input!!.toInt() }
        val givenInput = "123"
        val expected = 123

        // When
        val actual = classUnderTest(givenInput)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `Given Int to String lambda, Int when invoked then returns the integer as a string`() {
        // Given
        val classUnderTest = ExpressionFieldDataProvider<Int, String> { input -> input.toString() }
        val givenInput = 123
        val expected = "123"

        // When
        val actual = classUnderTest(givenInput)

        // Then
        assertEquals(expected, actual)
    }
}
