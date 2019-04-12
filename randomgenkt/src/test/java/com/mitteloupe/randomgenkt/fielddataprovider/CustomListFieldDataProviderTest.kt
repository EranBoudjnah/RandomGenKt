package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import com.nhaarman.mockitokotlin2.given
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Eran Boudjnah on 10/08/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class CustomListFieldDataProviderTest {
    private lateinit var cut: CustomListFieldDataProvider<*, String>

    @Mock
    private lateinit var fieldDataProvider: FieldDataProvider<Any, String>

    @Before
    fun setUp() {
        cut = CustomListFieldDataProvider(INSTANCES, fieldDataProvider)
    }

    @Test
    fun givenFixedSizeListOfRandomBytesWhenGenerateThenReturnsSameBytes() {
        // Given
        val expectedResult1 = "I'm the king of the world!"
        val expectedResult2 = "I'm on a boat!"
        val expectedResult3 = "I'm cold!"
        given(fieldDataProvider.invoke(null)).willReturn(expectedResult1, expectedResult2, expectedResult3)

        // When
        val result = cut.invoke()

        // Then
        assertEquals(listOf(expectedResult1, expectedResult2, expectedResult3), result)
    }

    private companion object {
        private const val INSTANCES = 3
    }
}