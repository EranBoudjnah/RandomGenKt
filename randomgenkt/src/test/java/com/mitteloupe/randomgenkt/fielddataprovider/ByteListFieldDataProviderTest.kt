package com.mitteloupe.randomgenkt.fielddataprovider

import java.util.Random
import org.junit.Assert.assertArrayEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ByteListFieldDataProviderTest {
    private lateinit var classUnderTest: ByteListFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Test
    fun givenFixedSizeListOfRandomBytesWhenGenerateThenReturnsSameBytes() {
        // Given
        val expectedResult = listOf(123.toByte(), 12.toByte(), 1.toByte()).toByteArray()

        doAnswer { invocation ->
            val result = invocation.getArgument<ByteArray>(0)
            result[0] = expectedResult[0]
            result[1] = expectedResult[1]
            result[2] = expectedResult[2]
            null
        }.`when`(random).nextBytes(any())

        classUnderTest = ByteListFieldDataProvider(random, minSize = 3)

        // When
        val result = classUnderTest()

        // Then
        assertArrayEquals(expectedResult, result)
    }

    @Test
    fun givenRangedSizeListOfRandomBytesWhenGenerateThenReturnsSameBytes() {
        // Given
        val expectedResult = listOf(123.toByte(), 12.toByte(), 1.toByte()).toByteArray()

        given(random.nextInt(5)).willReturn(2)
        doAnswer { invocation ->
            val result = invocation.getArgument<ByteArray>(0)
            result[0] = expectedResult[0]
            result[1] = expectedResult[1]
            result[2] = expectedResult[2]
            null
        }.`when`(random).nextBytes(any())

        classUnderTest = ByteListFieldDataProvider(random, minSize = 1, maxSize = 5)

        // When
        val result = classUnderTest()

        // Then
        assertArrayEquals(expectedResult, result)
    }
}
