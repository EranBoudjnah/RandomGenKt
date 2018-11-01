package com.mitteloupe.randomgenexample.domain

import com.mitteloupe.randomgen.RandomGen
import com.mitteloupe.randomgenexample.data.generator.PlanetarySystemGeneratorFactory
import com.mitteloupe.randomgenexample.data.model.planet.PlanetarySystem
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
class GeneratePlanetarySystemUseCaseTest {
	private lateinit var cut: GeneratePlanetarySystemUseCase

	lateinit var coroutineContextProvider: CoroutineContextProvider
	@Mock
	lateinit var planetarySystemGeneratorFactory: PlanetarySystemGeneratorFactory
	@Mock
	lateinit var planetarySystemGenerator: RandomGen<PlanetarySystem>

	@Before
	fun setUp() {
		coroutineContextProvider = testCoroutineContextProvider()
		whenever(planetarySystemGeneratorFactory.newPlanetarySystemGenerator).thenReturn(planetarySystemGenerator)

		cut = GeneratePlanetarySystemUseCase(coroutineContextProvider, planetarySystemGeneratorFactory)
	}

	@Test
	fun `Given generator with planetary system when execute then returns same planetary system`() {
		// Given
		val expectedResult = mock<PlanetarySystem>()
		var actualResult: PlanetarySystem? = null
		whenever(planetarySystemGenerator.generate()).thenReturn(expectedResult)

		// When
		runBlocking {
			cut.execute { planetarySystem -> actualResult = planetarySystem }
		}

		// Then
		assertEquals(expectedResult, actualResult)
	}
}