package com.mitteloupe.randomgenkt.fielddataprovider

import java.util.Random
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val FIRST_VALUE = "The First"
private const val LAST_VALUE = "Eternity"

@RunWith(MockitoJUnitRunner::class)
class GenericListFieldDataProviderTest {
    private lateinit var classUnderTest: GenericListFieldDataProvider<Any, String>

    @Mock
    private lateinit var random: Random

    private lateinit var values: List<String>

    @Before
    fun setUp() {
        values = listOf(FIRST_VALUE, "The Last", LAST_VALUE)

        classUnderTest = GenericListFieldDataProvider(random, values)
    }

    @Test
    fun `Given minimal random value when generate then returns first value`() {
        // Given
        given(random.nextInt(values.size)).willReturn(0)

        // When
        val result = classUnderTest()

        // Then
        assertEquals(FIRST_VALUE, result)
    }

    @Test
    fun `Given maximal random value when generate then returns last value`() {
        // Given
        given(random.nextInt(values.size)).willReturn(values.size - 1)

        // When
        val result = classUnderTest()

        // Then
        assertEquals(LAST_VALUE, result)
    }
}
