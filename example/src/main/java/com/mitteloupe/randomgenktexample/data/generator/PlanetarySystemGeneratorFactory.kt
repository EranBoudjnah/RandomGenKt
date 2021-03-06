package com.mitteloupe.randomgenktexample.data.generator

import androidx.core.util.Pair
import com.mitteloupe.randomgenkt.RandomGen
import com.mitteloupe.randomgenktexample.data.model.planet.Material
import com.mitteloupe.randomgenktexample.data.model.planet.Planet
import com.mitteloupe.randomgenktexample.data.model.planet.PlanetarySystem
import dagger.Reusable
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 28/08/2018.
 */
@Reusable
class PlanetarySystemGeneratorFactory
@Inject
constructor() {
    // Create a random star system with 2 to 4 planets
    val newPlanetarySystemGenerator by lazy {
        RandomGen.Builder<PlanetarySystem>()
            .ofClass<PlanetarySystem>()
            .withField("starAgeBillionYears")
            .returning(1f, 10f)
            .withField("starDiameterSunRadii")
            .returning(0.00001438, 250.0)
            .withField("starSolarMass")
            .returning(0.08, 150.0)
            .withField("planets")
            .returning(0, 15, newPlanetGenerator())
            .build()
    }

    private fun newPlanetGenerator() =
        RandomGen.Builder<Planet>()
            .ofClass<Planet>()
            .withField("id")
            .returningSequentialInteger()
            .withField("diameterEarthRatio")
            .returning(0.3f, 12f)
            .withField("solarMass")
            .returning(0.05f, 350f)
            .withField("orbitalPeriodYears")
            .returning(0.2f, 200f)
            .withField("rotationPeriodDays")
            .returning(0.3f, 250f)
            .withField("moons")
            .returning(0, 100)
            .withField("hasRings")
            .returningBoolean()
            .withField("atmosphere")
            .returning(0, 3, newMaterialGenerator())
            .build()

    private fun newMaterialGenerator(): RandomGen<Material> {
        val materialAr = listOf(Pair("Ar", 1))
        val materialCH4 = listOf(Pair("C", 1), Pair("H", 4))
        val materialCO2 = listOf(Pair("C", 1), Pair("O", 2))
        val materialH2 = listOf(Pair("H", 2))
        val materialHe = listOf(Pair("He", 1))
        val materialN2 = listOf(Pair("N", 2))
        val materialO2 = listOf(Pair("O", 2))

        return RandomGen.Builder<Material>()
            .ofClass<Material>()
            .withField("compound")
            .returning(listOf(materialAr, materialCH4, materialCO2, materialH2, materialHe, materialN2, materialO2))
            .build()
    }
}
