package com.mitteloupe.randomgenktexample.domain

import com.mitteloupe.randomgenkt.RandomGen
import com.mitteloupe.randomgenktexample.data.generator.PersonGeneratorFactory
import com.mitteloupe.randomgenktexample.data.model.person.Person
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
		given(personGeneratorFactory.newPersonGenerator).willReturn(personGenerator)

		cut = GeneratePersonUseCase(coroutineContextProvider, personGeneratorFactory)
	}

	@Test
	fun `Given generator with person when execute then returns same person`() {
		// Given
		val expectedResult = mock<Person>()
		var actualResult: Person? = null
		given(personGenerator.generate()).willReturn(expectedResult)

		// When
		runBlocking {
			cut.execute { person -> actualResult = person }
		}

		// Then
		assertEquals(expectedResult, actualResult)
	}
}