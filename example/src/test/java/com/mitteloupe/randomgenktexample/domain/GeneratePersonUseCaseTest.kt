package com.mitteloupe.randomgenktexample.domain

import com.mitteloupe.randomgenkt.RandomGen
import com.mitteloupe.randomgenktexample.data.generator.PersonGeneratorFactory
import com.mitteloupe.randomgenktexample.data.model.person.Person
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.given
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class GeneratePersonUseCaseTest {
    private lateinit var classUnderTest: GeneratePersonUseCase

    private lateinit var coroutineContextProvider: CoroutineContextProvider

    @Mock
    lateinit var personGeneratorFactory: PersonGeneratorFactory

    @Mock
    lateinit var personGenerator: RandomGen<Person>

    @Before
    fun setUp() {
        coroutineContextProvider = testCoroutineContextProvider()
        given(personGeneratorFactory.newPersonGenerator).willReturn(personGenerator)

        classUnderTest = GeneratePersonUseCase(coroutineContextProvider, personGeneratorFactory)
    }

    @Test
    fun `Given generator with person when execute then returns same person`() {
        // Given
        val expectedResult = mock<Person>()
        var actualResult: Person? = null
        given(personGenerator()).willReturn(expectedResult)

        // When
        runBlocking {
            classUnderTest.execute { person -> actualResult = person }
        }

        // Then
        assertEquals(expectedResult, actualResult)
    }
}
