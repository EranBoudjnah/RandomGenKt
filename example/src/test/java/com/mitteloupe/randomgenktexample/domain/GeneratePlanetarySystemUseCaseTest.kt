package com.mitteloupe.randomgenktexample.domain

import com.mitteloupe.randomgenkt.RandomGen
import com.mitteloupe.randomgenktexample.data.generator.PlanetarySystemGeneratorFactory
import com.mitteloupe.randomgenktexample.data.model.planet.PlanetarySystem
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
		given(planetarySystemGeneratorFactory.newPlanetarySystemGenerator).willReturn(planetarySystemGenerator)

		cut = GeneratePlanetarySystemUseCase(coroutineContextProvider, planetarySystemGeneratorFactory)
	}

	@Test
	fun `Given generator with planetary system when execute then returns same planetary system`() {
		// Given
		val expectedResult = mock<PlanetarySystem>()
		var actualResult: PlanetarySystem? = null
		given(planetarySystemGenerator.generate()).willReturn(expectedResult)

		// When
		runBlocking {
			cut.execute { planetarySystem -> actualResult = planetarySystem }
		}

		// Then
		assertEquals(expectedResult, actualResult)
	}
}