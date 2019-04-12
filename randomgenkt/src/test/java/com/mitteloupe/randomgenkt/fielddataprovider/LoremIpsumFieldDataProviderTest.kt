package com.mitteloupe.randomgenkt.fielddataprovider

import com.nhaarman.mockitokotlin2.given
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.Random

/**
 * Created by Eran Boudjnah on 10/08/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LoremIpsumFieldDataProviderTest {
    private lateinit var cut: LoremIpsumFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Test
    fun whenGenerateThenReturnsOneInstanceOfLoremIpsum() {
        // Given
        cut = LoremIpsumFieldDataProvider.getInstance()

        // When
        val result = cut.invoke()

        // Then
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et doloremagna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute iruredolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt inculpa qui officia deserunt mollit anim id est laborum.",
                result)
    }

    @Test
    fun givenLengthShorterThanWholeLoremIpsumWhenGenerateThenReturnsCorrectLengthOfLoremIpsum() {
        // Given
        cut = LoremIpsumFieldDataProvider.getInstance(39)

        // When
        val result = cut.invoke()

        // Then
        assertEquals("Lorem ipsum dolor sit amet, consectetur", result)
    }

    @Test
    fun givenLengthLongerThanWholeLoremIpsumWhenGenerateThenReturnsCorrectLengthOfLoremIpsum() {
        // Given
        cut = LoremIpsumFieldDataProvider.getInstance(449)

        // When
        val result = cut.invoke()

        // Then
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et doloremagna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute iruredolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt inculpa qui officia deserunt mollit anim id est laborum.\n" + "\nLorem",
                result)
    }

    @Test
    fun givenLengthRangeWhenGenerateThenReturnsCorrectLengthOfLoremIpsum() {
        // Given
        cut = LoremIpsumFieldDataProvider.getInstanceWithRange(random, 39, 41)
        given(random.nextInt(3)).willReturn(0)

        // When
        var result = cut.invoke()

        // Then
        assertEquals("Lorem ipsum dolor sit amet, consectetur", result)

        // Given
        given(random.nextInt(3)).willReturn(2)

        // When
        result = cut.invoke()

        // Then
        assertEquals("Lorem ipsum dolor sit amet, consectetur a", result)
    }

    @Test
    fun givenLengthRangeAndDelimiterWhenGenerateThenReturnsCorrectLengthOfLoremIpsum() {
        // Given
        cut = LoremIpsumFieldDataProvider.getInstanceWithRange(random, 444, 449, "**")
        given(random.nextInt(6)).willReturn(5)

        // When
        var result = cut.invoke()

        // Then
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et doloremagna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute iruredolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt inculpa qui officia deserunt mollit anim id est laborum." + "**Lorem",
                result)

        // Given
        given(random.nextInt(6)).willReturn(0)

        // When
        result = cut.invoke()

        // Then
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et doloremagna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute iruredolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt inculpa qui officia deserunt mollit anim id est laborum.",
                result)
    }
}