package com.mitteloupe.randomgenktexample.domain

import com.mitteloupe.randomgenktexample.data.generator.PlanetarySystemGeneratorFactory
import com.mitteloupe.randomgenktexample.data.model.planet.PlanetarySystem
import dagger.Reusable
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 30/10/2018.
 */
@Reusable
class GeneratePlanetarySystemUseCase
@Inject constructor(
	coroutineContextProvider: CoroutineContextProvider,
	planetarySystemGeneratorFactory: PlanetarySystemGeneratorFactory
) : BaseUseCase<PlanetarySystem>(coroutineContextProvider) {
	private val planetarySystemRandomGen by lazy { planetarySystemGeneratorFactory.newPlanetarySystemGenerator }

	override fun executeAsync() = planetarySystemRandomGen.generate()
}
