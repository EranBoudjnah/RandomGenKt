package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CustomListRangeFieldDataProviderTest {
    private lateinit var classUnderTest: CustomListRangeFieldDataProvider<*, String>

    @Mock
    private lateinit var random: Random

    @Mock
    private lateinit var fieldDataProvider: FieldDataProvider<Any, String>

    @Before
    fun setUp() {
        classUnderTest = CustomListRangeFieldDataProvider(
            random,
            MIN_INSTANCES,
            MAX_INSTANCES,
            fieldDataProvider
        )
    }

    @Test
    fun givenFixedSizeListOfRandomBytesWhenGenerateThenReturnsSameBytes() {
        // Given
        val expectedResult1 = "I'm the king of the world!"
        val expectedResult2 = "I'm on a boat!"
        val expectedResult3 = "I'm cold!"
        given(fieldDataProvider.invoke(null))
            .willReturn(expectedResult1, expectedResult2, expectedResult3)
        given(random.nextInt(5)).willReturn(2)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertEquals(listOf(expectedResult1, expectedResult2, expectedResult3), result)
    }

    private companion object {
        private const val MIN_INSTANCES = 1
        private const val MAX_INSTANCES = 5
    }
}
