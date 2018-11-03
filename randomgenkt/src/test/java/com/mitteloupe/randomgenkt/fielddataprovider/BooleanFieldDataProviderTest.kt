package com.mitteloupe.randomgenkt.fielddataprovider

import com.nhaarman.mockitokotlin2.whenever
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
		whenever(random.nextBoolean()).thenReturn(false)

		// When
		val result = cut.invoke()

		// Then
		assertFalse(result)
	}

	@Test
	fun givenTrueRandomValueWhenGenerateThenReturnsTrue() {
		// Given
		whenever(random.nextBoolean()).thenReturn(true)

		// When
		val result = cut.invoke()

		// Then
		assertTrue(result)
	}
}