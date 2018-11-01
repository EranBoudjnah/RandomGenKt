package com.mitteloupe.randomgenexample.domain

import com.mitteloupe.randomgenexample.data.generator.PlanetarySystemGeneratorFactory
import com.mitteloupe.randomgenexample.data.model.planet.PlanetarySystem
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 30/10/2018.
 */
class GeneratePlanetarySystemUseCase
@Inject
constructor(
	private val coroutineContextProvider: CoroutineContextProvider,
	planetarySystemGeneratorFactory: PlanetarySystemGeneratorFactory
) : BaseUseCase<PlanetarySystem>(coroutineContextProvider) {
	private var planetarySystemRandomGen = planetarySystemGeneratorFactory.newPlanetarySystemGenerator

	override suspend fun execute(callback: (PlanetarySystem) -> Unit) {
		this.callback = callback

		uiScope.launch {
			var planetarySystem: PlanetarySystem? = null

			withContext(coroutineContextProvider.io) {
				planetarySystem = executeAsync()
			}

			planetarySystem?.let(callback)
		}
	}

	fun executeAsync(): PlanetarySystem {
		return planetarySystemRandomGen.generate()
	}
}
