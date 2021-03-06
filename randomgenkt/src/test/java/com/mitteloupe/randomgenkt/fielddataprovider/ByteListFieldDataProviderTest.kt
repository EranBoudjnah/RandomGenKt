package com.mitteloupe.randomgenkt.fielddataprovider

import com.nhaarman.mockitokotlin2.given
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.junit.MockitoJUnitRunner
import java.util.Random

/**
 * Created by Eran Boudjnah on 10/08/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class ByteListFieldDataProviderTest {
    private lateinit var cut: ByteListFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Test
    fun givenFixedSizeListOfRandomBytesWhenGenerateThenReturnsSameBytes() {
        // Given
        val expectedResult = listOf(123.toByte(), 12.toByte(), 1.toByte())

        doAnswer { invocation ->
            val result = invocation.getArgument<ByteArray>(0)
            result[0] = expectedResult[0]
            result[1] = expectedResult[1]
            result[2] = expectedResult[2]
            null
        }.`when`(random).nextBytes(any())

        cut = ByteListFieldDataProvider.getInstanceWithSize(random, 3)

        // When
        val result = cut.invoke()

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun givenRangedSizeListOfRandomBytesWhenGenerateThenReturnsSameBytes() {
        // Given
        val expectedResult = listOf(123.toByte(), 12.toByte(), 1.toByte())

        given(random.nextInt(5)).willReturn(2)
        doAnswer { invocation ->
            val result = invocation.getArgument<ByteArray>(0)
            result[0] = expectedResult[0]
            result[1] = expectedResult[1]
            result[2] = expectedResult[2]
            null
        }.`when`(random).nextBytes(any())

        cut = ByteListFieldDataProvider.getInstanceWithSizeRange(random, 1, 5)

        // When
        val result = cut.invoke()

        // Then
        assertEquals(expectedResult, result)
    }
}