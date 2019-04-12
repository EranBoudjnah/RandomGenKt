package com.mitteloupe.randomgenkt.fielddataprovider

import com.nhaarman.mockitokotlin2.given
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.Arrays
import java.util.Random

/**
 * Created by Eran Boudjnah on 10/08/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class GenericListFieldDataProviderTest {
    private lateinit var cut: GenericListFieldDataProvider<Any, String>

    @Mock
    private lateinit var random: Random

    private lateinit var values: MutableList<String>

    @Before
    fun setUp() {
        values = Arrays.asList("The First", "The Last", "Eternity")

        cut = GenericListFieldDataProvider(random, values)
    }

    @Test
    fun givenMinRandomValueWhenGenerateThenReturnsFirstValue() {
        // Given
        given(random.nextDouble()).willReturn(0.0)

        // When
        val result = cut.invoke()

        // Then
        assertEquals("The First", result)
    }

    @Test
    fun givenMaxRandomValueWhenGenerateThenReturnsLastValue() {
        // Given
        given(random.nextDouble()).willReturn(0.99999999999)

        // When
        val result = cut.invoke()

        // Then
        assertEquals("Eternity", result)
    }

    @Test
    fun givenListIsModifiedWhenGenerateThenReturnsUnmodifiedValue() {
        // Given
        given(random.nextDouble()).willReturn(0.0)
        values[0] = "The Firstest"

        // When
        val result = cut.invoke()

        // Then
        assertEquals("The First", result)
    }
}