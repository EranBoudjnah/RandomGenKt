package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ConcatenateFieldDataProviderTest {
    private lateinit var classUnderTest: ConcatenateFieldDataProvider<TestClass>

    @Mock
    private lateinit var instance: TestClass

    @Mock
    private lateinit var fieldDataProvider1: FieldDataProvider<TestClass, String>

    @Mock
    private lateinit var fieldDataProvider2: FieldDataProvider<TestClass, String>

    @Before
    fun setUp() {
        given(fieldDataProvider1.invoke(instance)).willReturn("Test1")
        given(fieldDataProvider2.invoke(instance)).willReturn("Test2")
    }

    @Test
    fun givenFieldDataProvidersWhenGenerateThenReturnsConcatenatedOutput() {
        // Given
        classUnderTest = ConcatenateFieldDataProvider(fieldDataProvider1, fieldDataProvider2)

        // When
        val result = classUnderTest.invoke(instance)

        // Then
        assertEquals("Test1Test2", result)
    }

    @Test
    fun givenDelimiterAndFieldDataProvidersWhenGenerateThenReturnsDelimitedConcatenatedOutput() {
        // Given
        classUnderTest = ConcatenateFieldDataProvider(
            fieldDataProvider1,
            fieldDataProvider2,
            delimiter = ", "
        )

        // When
        val result = classUnderTest.invoke(instance)

        // Then
        assertEquals("Test1, Test2", result)
    }

    private inner class TestClass
}
