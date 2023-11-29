package com.mitteloupe.randomgenkt.fielddataprovider

import java.util.Random
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BooleanFieldDataProviderTest {
    private lateinit var classUnderTest: BooleanFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Before
    fun setUp() {
        classUnderTest = BooleanFieldDataProvider(random)
    }

    @Test
    fun givenFalseRandomValueWhenGenerateThenReturnsFalse() {
        // Given
        given(random.nextBoolean()).willReturn(false)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertFalse(result)
    }

    @Test
    fun givenTrueRandomValueWhenGenerateThenReturnsTrue() {
        // Given
        given(random.nextBoolean()).willReturn(true)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertTrue(result)
    }
}
