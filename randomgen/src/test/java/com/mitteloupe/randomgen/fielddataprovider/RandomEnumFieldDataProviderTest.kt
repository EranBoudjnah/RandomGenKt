package com.mitteloupe.randomgen.fielddataprovider

import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.Random

/**
 * Created by Eran Boudjnah on 11/08/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class RandomEnumFieldDataProviderTest {
	private lateinit var cut: RandomEnumFieldDataProvider<Any, MagicColors>

	@Mock
	private lateinit var random: Random

	@Before
	fun setUp() {
		cut = RandomEnumFieldDataProvider(random, MagicColors::class.java)
	}

	@Test
	fun givenRandomDoubleValueWhenGenerateThenReturnsIntegerValue() {
		// Given
		whenever(random.nextDouble()).thenReturn(0.0)

		// When
		var result = cut.generate()

		// Then
		assertEquals(MagicColors.WHITE, result)

		// Given
		whenever(random.nextDouble()).thenReturn(0.5)

		// When
		result = cut.generate()

		// Then
		assertEquals(MagicColors.BLACK, result)

		// Given
		whenever(random.nextDouble()).thenReturn(0.99999999999)

		// When
		result = cut.generate()

		// Then
		assertEquals(MagicColors.GREEN, result)
	}

	private enum class MagicColors {
		WHITE,
		BLUE,
		BLACK,
		RED,
		GREEN
	}
}