package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PaddedFieldDataProviderTest {
    private lateinit var classUnderTest: PaddedFieldDataProvider<TestClass>

    @Mock
    private lateinit var instance: TestClass

    @Mock
    private lateinit var fieldDataProvider: FieldDataProvider<TestClass, Any>

    @Test
    fun givenSinglePaddingCharacterAndMinimumLengthWhenGenerateThenReturnsExpectedString() {
        classUnderTest = PaddedFieldDataProvider(3, "0", fieldDataProvider)
        given(fieldDataProvider.invoke(instance)).willReturn("7")

        val result = classUnderTest.invoke(instance)

        assertEquals("007", result)
    }

    @Test
    fun givenPaddingStringAndMinimumLengthWhenGenerateThenReturnsExpectedString() {
        classUnderTest = PaddedFieldDataProvider(3, "000", fieldDataProvider)
        given(fieldDataProvider.invoke(instance)).willReturn("7")

        val result = classUnderTest.invoke(instance)

        assertEquals("007", result)
    }

    @Test
    fun givenStringAsLongAsPaddingWhenGenerateThenReturnsUnmodifiedString() {
        classUnderTest = PaddedFieldDataProvider(3, "000", fieldDataProvider)
        given(fieldDataProvider.invoke(instance)).willReturn("123")

        val result = classUnderTest.invoke(instance)

        assertEquals("123", result)
    }

    @Test
    fun givenStringLongerThanPaddingWhenGenerateThenReturnsUnmodifiedString() {
        classUnderTest = PaddedFieldDataProvider(3, "000", fieldDataProvider)
        given(fieldDataProvider.invoke(instance)).willReturn("1337")

        val result = classUnderTest.invoke(instance)

        assertEquals("1337", result)
    }

    private inner class TestClass
}
