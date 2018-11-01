package com.mitteloupe.randomgenexample.domain

import com.mitteloupe.randomgen.RandomGen
import com.mitteloupe.randomgenexample.data.generator.PersonGeneratorFactory
import com.mitteloupe.randomgenexample.data.model.person.Person
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
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
class GeneratePersonUseCaseTest {
	private lateinit var cut: GeneratePersonUseCase

	lateinit var coroutineContextProvider: CoroutineContextProvider
	@Mock
	lateinit var personGeneratorFactory: PersonGeneratorFactory
	@Mock
	lateinit var personGenerator: RandomGen<Person>

	@Before
	fun setUp() {
		coroutineContextProvider = testCoroutineContextProvider()
		whenever(personGeneratorFactory.newPersonGenerator).thenReturn(personGenerator)

		cut = GeneratePersonUseCase(coroutineContextProvider, personGeneratorFactory)
	}

	@Test
	fun `Given generator with person when execute then returns same person`() {
		// Given
		val expectedResult = mock<Person>()
		var actualResult: Person? = null
		whenever(personGenerator.generate()).thenReturn(expectedResult)

		// When
		runBlocking {
			cut.execute { person -> actualResult = person }
		}

		// Then
		assertEquals(expectedResult, actualResult)
	}
}