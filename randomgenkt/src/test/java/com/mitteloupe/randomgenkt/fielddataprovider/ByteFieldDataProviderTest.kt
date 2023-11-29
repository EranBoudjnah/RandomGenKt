package com.mitteloupe.randomgenkt.fielddataprovider

import java.util.Random
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ByteFieldDataProviderTest {
    private lateinit var classUnderTest: ByteFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Before
    fun setUp() {
        classUnderTest = ByteFieldDataProvider(random)
    }

    @Test
    fun givenRandomByteValueWhenGenerateThenReturnsSameByte() {
        // Given
        doAnswer { invocation ->
            invocation.getArgument<ByteArray>(0)[0] = 113
            null
        }.`when`(random).nextBytes(any())

        // When
        val result = classUnderTest.invoke()

        // Then
        assertEquals(113.toByte(), result)
    }
}
