package com.mitteloupe.randomgenkt.fielddataprovider

import org.junit.Assert.assertEquals
import org.junit.Before
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
class ByteFieldDataProviderTest {
    private lateinit var cut: ByteFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Before
    fun setUp() {
        cut = ByteFieldDataProvider(random)
    }

    @Test
    fun givenRandomByteValueWhenGenerateThenReturnsSameByte() {
        // Given
        doAnswer { invocation ->
            invocation.getArgument<ByteArray>(0)[0] = 113
            null
        }.`when`(random).nextBytes(any())

        // When
        val result = cut.invoke()

        // Then
        assertEquals(113.toByte(), result)
    }
}