package com.mitteloupe.randomgenkt.fielddataprovider

import com.nhaarman.mockitokotlin2.given
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.Random

/**
 * Created by Eran Boudjnah on 10/08/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class BooleanFieldDataProviderTest {
    private lateinit var cut: BooleanFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Before
    fun setUp() {
        cut = BooleanFieldDataProvider(random)
    }

    @Test
    fun givenFalseRandomValueWhenGenerateThenReturnsFalse() {
        // Given
        given(random.nextBoolean()).willReturn(false)

        // When
        val result = cut.invoke()

        // Then
        assertFalse(result)
    }

    @Test
    fun givenTrueRandomValueWhenGenerateThenReturnsTrue() {
        // Given
        given(random.nextBoolean()).willReturn(true)

        // When
        val result = cut.invoke()

        // Then
        assertTrue(result)
    }
}