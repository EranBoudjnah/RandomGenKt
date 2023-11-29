package com.mitteloupe.randomgenkt.fielddataprovider

import java.util.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoremIpsumFieldDataProviderTest {
    private lateinit var classUnderTest: LoremIpsumFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Test
    fun `When generate then returns one instance of Lorem Ipsum`() {
        // Given
        classUnderTest = LoremIpsumFieldDataProvider()

        // When
        val result = classUnderTest.invoke()

        // Then
        assertEquals(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis " +
                "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt " +
                "in culpa qui officia deserunt mollit anim id est laborum.",
            result
        )
    }

    @Test
    fun givenLengthShorterThanWholeLoremIpsumWhenGenerateThenReturnsCorrectLengthOfLoremIpsum() {
        // Given
        classUnderTest = LoremIpsumFieldDataProvider(minimumLength = 39)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertEquals("Lorem ipsum dolor sit amet, consectetur", result)
    }

    @Test
    fun givenLengthLongerThanWholeLoremIpsumWhenGenerateThenReturnsCorrectLengthOfLoremIpsum() {
        // Given
        classUnderTest = LoremIpsumFieldDataProvider(minimumLength = 449)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertEquals(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis " +
                "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt " +
                "in culpa qui officia deserunt mollit anim id est laborum.\n\nLo",
            result
        )
    }

    @Test
    fun givenLengthRangeWhenGenerateThenReturnsCorrectLengthOfLoremIpsum() {
        // Given
        classUnderTest = LoremIpsumFieldDataProvider(
            random,
            minimumLength = 39,
            maximumLength = 41
        )
        given(random.nextInt(3)).willReturn(0)

        // When
        var result = classUnderTest.invoke()

        // Then
        assertEquals("Lorem ipsum dolor sit amet, consectetur", result)

        // Given
        given(random.nextInt(3)).willReturn(2)

        // When
        result = classUnderTest.invoke()

        // Then
        assertEquals("Lorem ipsum dolor sit amet, consectetur a", result)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given length, range, delimiter, minimum random when generate then returns substring of Lorem Ipsum`() {
        // Given
        classUnderTest = rangedLoremIpsumFieldDataProvider()
        given(random.nextInt(6)).willReturn(0)

        // When
        val result = classUnderTest()

        // Then
        assertEquals(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis " +
                "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt " +
                "in culpa qui officia deserunt mollit anim id est laborum.",
            result
        )
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given length, range, delimiter, maximum random when generate then returns substring of Lorem Ipsum`() {
        // Given
        classUnderTest = rangedLoremIpsumFieldDataProvider()

        given(random.nextInt(6)).willReturn(5)

        // When
        val result = classUnderTest()

        // Then
        assertEquals(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis " +
                "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt " +
                "in culpa qui officia deserunt mollit anim id est laborum.**Lor",
            result
        )
    }

    private fun rangedLoremIpsumFieldDataProvider() =
        LoremIpsumFieldDataProvider<Any>(
            random,
            minimumLength = 445,
            maximumLength = 450,
            paragraphDelimiter = "**"
        )
}
