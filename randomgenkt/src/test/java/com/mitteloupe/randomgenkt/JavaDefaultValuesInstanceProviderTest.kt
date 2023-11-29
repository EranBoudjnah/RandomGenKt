package com.mitteloupe.randomgenkt

import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection.empty
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class JavaDefaultValuesInstanceProviderTest {
    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given constructor with parameters when provideInstance then returns instance with default values`() {
        // Given
        val classUnderTest = JavaDefaultValuesInstanceProvider(JavaTestClass::class.java)

        // When
        val actual = classUnderTest()

        // Then
        assertEquals(0.toByte(), actual.byteField)
        assertEquals(0.toShort(), actual.shortField)
        assertEquals(0, actual.intField.toLong())
        assertEquals(0, actual.longField)
        assertEquals(0f, actual.floatField, 0f)
        assertEquals(0.0, actual.doubleField, 0.0)
        assertFalse(actual.booleanField)
        assertEquals("", actual.stringField)
        assertThat(actual.objectField, `is`(instanceOf(Any::class.java)))
        assertThat(actual.objects, `is`(empty()))
    }

    @Test
    fun `Given no defined constructor when provideInstance then returns instance`() {
        // Given
        val classUnderTest =
            JavaDefaultValuesInstanceProvider(TestClassWithNoConstructor::class.java)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertNotNull(result)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given public constructor with no parameters when provide instance then uses constructor`() {
        // Given
        val classUnderTest = JavaDefaultValuesInstanceProvider(TestClassWithNoFields::class.java)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertTrue(result.isSet)
    }

    private interface TestClass

    private class TestClassWithNoConstructor : TestClass

    private class TestClassWithNoFields : TestClass {
        val isSet: Boolean = true
    }
}
