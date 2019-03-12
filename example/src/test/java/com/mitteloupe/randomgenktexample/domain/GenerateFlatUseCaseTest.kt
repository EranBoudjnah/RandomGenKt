package com.mitteloupe.randomgenktexample.domain

import com.mitteloupe.randomgenkt.RandomGen
import com.mitteloupe.randomgenktexample.data.generator.FlatGeneratorFactory
import com.mitteloupe.randomgenktexample.data.model.flat.Flat
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Eran Boudjnah on 01/11/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class GenerateFlatUseCaseTest {
	private lateinit var cut: GenerateFlatUseCase

	lateinit var coroutineContextProvider: CoroutineContextProvider
	@Mock
	lateinit var flatGeneratorFactory: FlatGeneratorFactory
	@Mock
	lateinit var flatGenerator: RandomGen<Flat>

	@Before
	fun setUp() {
		coroutineContextProvider = testCoroutineContextProvider()
		given(flatGeneratorFactory.newFlatGenerator).willReturn(flatGenerator)

		cut = GenerateFlatUseCase(coroutineContextProvider, flatGeneratorFactory)
	}

	@Test
	fun `Given generator with flat when execute then returns same flat`() {
		// Given
		val expectedResult = mock<Flat>()
		var actualResult: Flat? = null
		given(flatGenerator.generate()).willReturn(expectedResult)

		// When
		runBlocking {
			cut.execute { flat -> actualResult = flat }
		}

		// Then
		assertEquals(expectedResult, actualResult)
	}
}