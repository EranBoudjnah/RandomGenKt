package com.mitteloupe.randomgenktexample.domain

import com.mitteloupe.randomgenkt.RandomGen
import com.mitteloupe.randomgenktexample.data.generator.FlatGeneratorFactory
import com.mitteloupe.randomgenktexample.data.model.flat.Flat
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
class GenerateFlatUseCaseTest {
    private lateinit var classUnderTest: GenerateFlatUseCase

    private lateinit var coroutineContextProvider: CoroutineContextProvider

    @Mock
    lateinit var flatGeneratorFactory: FlatGeneratorFactory

    @Mock
    lateinit var flatGenerator: RandomGen<Flat>

    @Before
    fun setUp() {
        coroutineContextProvider = testCoroutineContextProvider()
        given(flatGeneratorFactory.newFlatGenerator).willReturn(flatGenerator)

        classUnderTest = GenerateFlatUseCase(coroutineContextProvider, flatGeneratorFactory)
    }

    @Test
    fun `Given generator with flat when execute then returns same flat`() {
        // Given
        val expectedResult = mock<Flat>()
        var actualResult: Flat? = null
        given(flatGenerator()).willReturn(expectedResult)

        // When
        runBlocking {
            classUnderTest.execute { flat -> actualResult = flat }
        }

        // Then
        assertEquals(expectedResult, actualResult)
    }
}
