package com.mitteloupe.randomgenkt.fielddataprovider

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class ExplicitFieldDataProviderTest {
    @Test
    fun givenExplicitStringWhenGenerateThenReturnsSameValue() {
        // Given
        val expectedResult = "Thou shall not pass!"
        val classUnderTest = ExplicitFieldDataProvider<Any, String>(expectedResult)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun givenExplicitObjectWhenGenerateThenReturnsSameValue() {
        // Given
        val expectedResult = TestClass()
        val classUnderTest = ExplicitFieldDataProvider<Any, TestClass>(expectedResult)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertSame(expectedResult, result)
    }

    private class TestClass
}
