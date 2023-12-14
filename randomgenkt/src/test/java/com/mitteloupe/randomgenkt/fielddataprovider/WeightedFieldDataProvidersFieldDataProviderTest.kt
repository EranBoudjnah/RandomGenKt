package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

private const val FIRST_VALUE = "Superman"
private const val SECOND_VALUE = "Spider Man"
private const val THIRD_VALUE = "Batman"

private typealias ClassUnderTest<OUTPUT_CLASS> =
    WeightedFieldDataProvidersFieldDataProvider<OUTPUT_CLASS, String>

@RunWith(MockitoJUnitRunner::class)
class WeightedFieldDataProvidersFieldDataProviderTest {
    private lateinit var classUnderTest: ClassUnderTest<TestClass>

    @Mock
    private lateinit var random: Random

    @Mock
    private lateinit var fieldDataProvider: FieldDataProvider<TestClass, String>

    @Before
    fun setUp() {
        given(fieldDataProvider.invoke(any(TestClass::class.java))).willReturn(FIRST_VALUE)

        classUnderTest = ClassUnderTest(random, fieldDataProvider)
    }

    @Test
    fun givenAnyInstanceWhenGenerateThenReturnsFirstValue() {
        // Given
        val instance = TestClass()

        // When
        val result = classUnderTest(instance)

        // Then
        assertEquals(FIRST_VALUE, result)
    }

    @Test
    fun `Given second FieldDataProvider,any instance,0_7 when invoked then returns third value`() {
        // Given
        val instance = TestClass()
        val fieldDataProvider2: FieldDataProvider<TestClass, String> = mock()
        classUnderTest.addFieldDataProvider(fieldDataProvider2, 2.0)
        val fieldDataProvider3: FieldDataProvider<TestClass, String> = mock()
        given(fieldDataProvider3(instance)).willReturn(THIRD_VALUE)
        classUnderTest.addFieldDataProvider(fieldDataProvider3, 3.0)
        given(random.nextDouble()).willReturn(0.7)

        // When
        val result = classUnderTest(instance)

        // Then
        assertEquals(THIRD_VALUE, result)
    }

    @Test
    fun `Given second FieldDataProvider,any instance,0_3 when invoked then returns second value`() {
        // Given
        val instance = TestClass()
        val fieldDataProvider2: FieldDataProvider<TestClass, String> = mock()
        given(fieldDataProvider2(instance)).willReturn(SECOND_VALUE)
        classUnderTest.addFieldDataProvider(fieldDataProvider2, 2.0)
        val fieldDataProvider3: FieldDataProvider<TestClass, String> = mock()
        classUnderTest.addFieldDataProvider(fieldDataProvider3, 3.0)
        given(random.nextDouble()).willReturn(0.3)

        // When
        val result = classUnderTest(instance)

        // Then
        assertEquals(SECOND_VALUE, result)
    }

    @Test
    fun `Given second FieldDataProvider,any instance,0_1 when invoked then returns first value`() {
        // Given
        val instance = TestClass()
        val fieldDataProvider2: FieldDataProvider<TestClass, String> = mock()
        classUnderTest.addFieldDataProvider(fieldDataProvider2, 2.0)
        val fieldDataProvider3: FieldDataProvider<TestClass, String> = mock()
        classUnderTest.addFieldDataProvider(fieldDataProvider3, 3.0)
        given(random.nextDouble()).willReturn(0.1)

        // When
        val result = classUnderTest(instance)

        // Then
        assertEquals(FIRST_VALUE, result)
    }

    private class TestClass
}
