package com.mitteloupe.randomgenexample.domain

import com.mitteloupe.randomgenexample.data.planet.PlanetarySystem
import com.mitteloupe.randomgenexample.generator.PlanetarySystemGeneratorFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Eran Boudjnah on 30/10/2018.
 */
class GeneratePlanetarySystemUseCase(planetarySystemGeneratorFactory: PlanetarySystemGeneratorFactory): BaseUseCase<PlanetarySystem>() {
	private var planetarySystemRandomGen = planetarySystemGeneratorFactory.newPlanetarySystemGenerator

	override suspend fun execute(callback: (PlanetarySystem) -> Unit) {
		this.callback = callback

		uiScope.launch {
			var planetarySystem: PlanetarySystem? = null

			withContext(Dispatchers.IO) {
				planetarySystem = executeAsync()
			}

			planetarySystem?.let(callback)
		}
	}

	fun executeAsync(): PlanetarySystem {
		return planetarySystemRandomGen.generate()
	}
}
