package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.UuidGenerator
import com.nhaarman.mockitokotlin2.given
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Eran Boudjnah on 11/08/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class UuidFieldDataProviderTest {
    private lateinit var cut: UuidFieldDataProvider<Any>

    @Mock
    private lateinit var uuidGenerator: UuidGenerator

    @Before
    fun setUp() {
        cut = UuidFieldDataProvider(uuidGenerator)
    }

    @Test
    fun givenAUuidWhenGenerateThenReturnsGeneratedValue() {
        // Given
        val expectedValue = "c9569006-9d99-11e8-98d0-529269fb1459"
        given(uuidGenerator.randomUUID()).willReturn(expectedValue)

        // When
        val result = cut.invoke()

        // Then
        assertEquals(expectedValue, result)
    }
}