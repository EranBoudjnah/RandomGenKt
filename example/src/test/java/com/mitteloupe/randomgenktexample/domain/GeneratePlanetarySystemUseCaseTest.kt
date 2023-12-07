package com.mitteloupe.randomgenktexample.domain

import com.mitteloupe.randomgenkt.RandomGen
import com.mitteloupe.randomgenktexample.data.generator.PlanetarySystemGeneratorFactory
import com.mitteloupe.randomgenktexample.data.model.planet.PlanetarySystem
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
class GeneratePlanetarySystemUseCaseTest {
    private lateinit var classUnderTest: GeneratePlanetarySystemUseCase

    private lateinit var coroutineContextProvider: CoroutineContextProvider

    @Mock
    lateinit var planetarySystemGeneratorFactory: PlanetarySystemGeneratorFactory

    @Mock
    lateinit var planetarySystemGenerator: RandomGen<PlanetarySystem>

    @Before
    fun setUp() {
        coroutineContextProvider = testCoroutineContextProvider()
        given(planetarySystemGeneratorFactory.planetarySystemGenerator).willReturn(
            planetarySystemGenerator
        )

        classUnderTest = GeneratePlanetarySystemUseCase(
            coroutineContextProvider,
            planetarySystemGeneratorFactory
        )
    }

    @Test
    fun `Given generator with planetary system when execute then returns same planetary system`() {
        // Given
        val expectedResult = mock<PlanetarySystem>()
        var actualResult: PlanetarySystem? = null
        given(planetarySystemGenerator()).willReturn(expectedResult)

        // When
        runBlocking {
            classUnderTest.execute { planetarySystem -> actualResult = planetarySystem }
        }

        // Then
        assertEquals(expectedResult, actualResult)
    }
}
