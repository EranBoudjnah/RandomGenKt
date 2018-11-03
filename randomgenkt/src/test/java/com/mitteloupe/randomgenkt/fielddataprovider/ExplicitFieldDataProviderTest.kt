package com.mitteloupe.randomgenkt.fielddataprovider

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

/**
 * Created by Eran Boudjnah on 12/08/2018.
 */
class ExplicitFieldDataProviderTest {
	@Test
	fun givenExplicitStringWhenGenerateThenReturnsSameValue() {
		// Given
		val expectedResult = "Thou shall not pass!"
		val cut = ExplicitFieldDataProvider<Any, String>(expectedResult)

		// When
		val result = cut.invoke()

		// Then
		assertEquals(expectedResult, result)
	}

	@Test
	fun givenExplicitObjectWhenGenerateThenReturnsSameValue() {
		// Given
		val expectedResult = TestClass()
		val cut = ExplicitFieldDataProvider<Any, TestClass>(expectedResult)

		// When
		val result = cut.invoke()

		// Then
		assertSame(expectedResult, result)
	}

	private class TestClass
}