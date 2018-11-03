package com.mitteloupe.randomgenkt.fielddataprovider

import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.util.Arrays
import java.util.Random



/**
 * Created by Eran Boudjnah on 11/08/2018.
 */
@RunWith(Parameterized::class)
class RandomEnumFieldDataProviderTest(
	private val randomValue: Int,
	private val expectedColor: MagicColors?
) {
	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<*>> {
			return Arrays.asList(
				arrayOf(0, MagicColors.WHITE),
				arrayOf(2, MagicColors.BLACK),
				arrayOf(4, MagicColors.GREEN)
			)
		}
	}

	private lateinit var cut: RandomEnumFieldDataProvider<Any, MagicColors>

	@get:Rule
	val mockitoRule: MockitoRule = MockitoJUnit.rule()

	@Mock
	private lateinit var random: Random

	@Before
	fun setUp() {
		cut = RandomEnumFieldDataProvider(random, MagicColors::class.java)
	}

	@Test
	fun givenRandomIntValueWhenGenerateThenReturnsIntegerValue() {
		// Given
		whenever(random.nextInt(5)).thenReturn(randomValue)

		// When
		val result = cut.invoke()

		// Then
		assertEquals(expectedColor, result)
	}

	enum class MagicColors {
		WHITE,
		BLUE,
		BLACK,
		RED,
		GREEN
	}
}