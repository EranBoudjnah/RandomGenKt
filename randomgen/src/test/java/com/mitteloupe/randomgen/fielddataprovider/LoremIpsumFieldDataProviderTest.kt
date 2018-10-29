package com.mitteloupe.randomgen.fielddataprovider

import com.nhaarman.mockitokotlin2.whenever
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
		val result = cut.generate()

		// Then
		assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et doloremagna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute iruredolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt inculpa qui officia deserunt mollit anim id est laborum.",
			result)
	}

	@Test
	fun givenLengthShorterThanWholeLoremIpsumWhenGenerateThenReturnsCorrectLengthOfLoremIpsum() {
		// Given
		cut = LoremIpsumFieldDataProvider.getInstance(39)

		// When
		val result = cut.generate()

		// Then
		assertEquals("Lorem ipsum dolor sit amet, consectetur", result)
	}

	@Test
	fun givenLengthLongerThanWholeLoremIpsumWhenGenerateThenReturnsCorrectLengthOfLoremIpsum() {
		// Given
		cut = LoremIpsumFieldDataProvider.getInstance(449)

		// When
		val result = cut.generate()

		// Then
		assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et doloremagna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute iruredolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt inculpa qui officia deserunt mollit anim id est laborum.\n" + "\nLorem",
			result)
	}

	@Test
	fun givenLengthRangeWhenGenerateThenReturnsCorrectLengthOfLoremIpsum() {
		// Given
		cut = LoremIpsumFieldDataProvider.getInstanceWithRange(random, 39, 41)
		whenever(random.nextInt(3)).thenReturn(0)

		// When
		var result = cut.generate()

		// Then
		assertEquals("Lorem ipsum dolor sit amet, consectetur", result)

		// Given
		whenever(random.nextInt(3)).thenReturn(2)

		// When
		result = cut.generate()

		// Then
		assertEquals("Lorem ipsum dolor sit amet, consectetur a", result)
	}

	@Test
	fun givenLengthRangeAndDelimiterWhenGenerateThenReturnsCorrectLengthOfLoremIpsum() {
		// Given
		cut = LoremIpsumFieldDataProvider.getInstanceWithRange(random, 444, 449, "**")
		whenever(random.nextInt(6)).thenReturn(5)

		// When
		var result = cut.generate()

		// Then
		assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et doloremagna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute iruredolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt inculpa qui officia deserunt mollit anim id est laborum." + "**Lorem",
			result)

		// Given
		whenever(random.nextInt(6)).thenReturn(0)

		// When
		result = cut.generate()

		// Then
		assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et doloremagna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute iruredolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt inculpa qui officia deserunt mollit anim id est laborum.",
			result)
	}
}