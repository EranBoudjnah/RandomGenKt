package com.mitteloupe.randomgen.fielddataprovider

import com.mitteloupe.randomgen.FieldDataProvider
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Eran Boudjnah on 02/09/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class ConcatenateFieldDataProviderTest {
	private lateinit var cut: ConcatenateFieldDataProvider<TestClass>

	@Mock
	private lateinit var instance: TestClass
	@Mock
	private lateinit var fieldDataProvider1: FieldDataProvider<TestClass, String>
	@Mock
	private lateinit var fieldDataProvider2: FieldDataProvider<TestClass, String>

	@Before
	fun setUp() {
		whenever(fieldDataProvider1.generate(instance)).thenReturn("Test1")
		whenever(fieldDataProvider2.generate(instance)).thenReturn("Test2")
	}

	@Test
	fun givenFieldDataProvidersWhenGenerateThenReturnsConcatenatedOutput() {
		// Given
		cut = ConcatenateFieldDataProvider(fieldDataProvider1, fieldDataProvider2)

		// When
		val result = cut.generate(instance)

		// Then
		assertEquals("Test1Test2", result)
	}

	@Test
	fun givenDelimiterAndFieldDataProvidersWhenGenerateThenReturnsDelimitedConcatenatedOutput() {
		// Given
		cut = ConcatenateFieldDataProvider(fieldDataProvider1, fieldDataProvider2, delimiter = ", ")

		// When
		val result = cut.generate(instance)

		// Then
		assertEquals("Test1, Test2", result)
	}

	private inner class TestClass
}