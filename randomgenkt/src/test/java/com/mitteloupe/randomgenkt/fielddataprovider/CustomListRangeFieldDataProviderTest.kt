package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import com.nhaarman.mockitokotlin2.whenever
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
class CustomListRangeFieldDataProviderTest {

	private lateinit var cut: CustomListRangeFieldDataProvider<*, String>

	@Mock
	private lateinit var random: Random
	@Mock
	private lateinit var fieldDataProvider: FieldDataProvider<Any, String>

	@Before
	fun setUp() {
		cut = CustomListRangeFieldDataProvider(random, MIN_INSTANCES, MAX_INSTANCES, fieldDataProvider)
	}

	@Test
	fun givenFixedSizeListOfRandomBytesWhenGenerateThenReturnsSameBytes() {
		// Given
		val expectedResult1 = "I'm the king of the world!"
		val expectedResult2 = "I'm on a boat!"
		val expectedResult3 = "I'm cold!"
		whenever(fieldDataProvider.invoke(null)).thenReturn(expectedResult1, expectedResult2, expectedResult3)
		whenever(random.nextInt(5)).thenReturn(2)

		// When
		val result = cut.invoke()

		// Then
		assertEquals(Arrays.asList(expectedResult1, expectedResult2, expectedResult3), result)
	}

	private companion object {
		private const val MIN_INSTANCES = 1
		private const val MAX_INSTANCES = 5
	}
}