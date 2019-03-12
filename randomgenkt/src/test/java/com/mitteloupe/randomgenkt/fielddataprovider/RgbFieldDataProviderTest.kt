package com.mitteloupe.randomgenkt.fielddataprovider

import com.nhaarman.mockitokotlin2.given
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.Random

/**
 * Created by Eran Boudjnah on 11/08/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class RgbFieldDataProviderTest {
	private lateinit var cut: RgbFieldDataProvider<Any>

	@Mock
	private lateinit var random: Random

	@Test
	fun givenARandomGeneratorAndAlphaWhenGenerateThenReturnsRGBAValue() {
		// Given
		cut = RgbFieldDataProvider(random, true)
		givenRandomValues(173, 250, 17, 222)

		// When
		val result = cut.invoke()

		// Then
		assertEquals("#deadfa11", result)
	}

	@Test
	fun givenARandomGeneratorAndNoAlphaWhenGenerateThenReturnsRGBValue() {
		// Given
		cut = RgbFieldDataProvider(random, false)
		givenRandomValues(176, 85, 237)

		// When
		val result = cut.invoke()

		// Then
		assertEquals("#b055ed", result)
	}

	private fun givenRandomValues(value: Int, vararg moreValues: Int) {
		given(random.nextInt(255)).willReturn(value, *moreValues.toTypedArray())
	}
}