package com.mitteloupe.randomgen.fielddataprovider

import com.mitteloupe.randomgen.FieldDataProvider
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Eran Boudjnah on 04/09/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class PaddedFieldDataProviderTest {
	private lateinit var cut: PaddedFieldDataProvider<TestClass>

	@Mock
	private lateinit var instance: TestClass
	@Mock
	private lateinit var fieldDataProvider: FieldDataProvider<TestClass, Any>

	@Test
	fun givenSinglePaddingCharacterAndMinimumLengthWhenGenerateThenReturnsExpectedString() {
		cut = PaddedFieldDataProvider(3, "0", fieldDataProvider)
		whenever(fieldDataProvider.generate(instance)).thenReturn("7")

		val result = cut.generate(instance)

		assertEquals("007", result)
	}

	@Test
	fun givenPaddingStringAndMinimumLengthWhenGenerateThenReturnsExpectedString() {
		cut = PaddedFieldDataProvider(3, "000", fieldDataProvider)
		whenever(fieldDataProvider.generate(instance)).thenReturn("7")

		val result = cut.generate(instance)

		assertEquals("007", result)
	}

	@Test
	fun givenStringAsLongAsPaddingWhenGenerateThenReturnsUnmodifiedString() {
		cut = PaddedFieldDataProvider(3, "000", fieldDataProvider)
		whenever(fieldDataProvider.generate(instance)).thenReturn("123")

		val result = cut.generate(instance)

		assertEquals("123", result)
	}

	@Test
	fun givenStringLongerThanPaddingWhenGenerateThenReturnsUnmodifiedString() {
		cut = PaddedFieldDataProvider(3, "000", fieldDataProvider)
		whenever(fieldDataProvider.generate(instance)).thenReturn("1337")

		val result = cut.generate(instance)

		assertEquals("1337", result)
	}

	private inner class TestClass
}