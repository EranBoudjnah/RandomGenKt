package com.mitteloupe.randomgenktexample.data.generator

import com.mitteloupe.randomgenkt.RandomGen
import com.mitteloupe.randomgenkt.builder.RandomGenBuilder
import com.mitteloupe.randomgenkt.model.ProvidingMethod
import com.mitteloupe.randomgenktexample.data.model.planet.Material
import com.mitteloupe.randomgenktexample.data.model.planet.Planet
import com.mitteloupe.randomgenktexample.data.model.planet.PlanetarySystem
import javax.inject.Inject

private val materialAr = arrayOf("Ar" to 1)
private val materialCH4 = arrayOf("C" to 1, "H" to 4)
private val materialCO2 = arrayOf("C" to 1, "O" to 2)
private val materialH2 = arrayOf("H" to 2)
private val materialHe = arrayOf("He" to 1)
private val materialN2 = arrayOf("N" to 2)
private val materialO2 = arrayOf("O" to 2)

class PlanetarySystemGeneratorFactory @Inject constructor() {
    val planetarySystemGenerator by lazy {
        RandomGenBuilder<PlanetarySystem>()
            .ofKotlinClass<PlanetarySystem>()
            .withField("starAgeBillionYears")
            .returning(1f, 10f)
            .withField("starDiameterSunRadii")
            .returning(0.00001438, 250.0)
            .withField("starSolarMass")
            .returning(0.08, 150.0)
            .withField("planets")
            .returning(planetGenerator(), minimumInstances = 0, maximumInstances = 15)
            .build()
    }

    private fun planetGenerator() = RandomGenBuilder<Planet>()
        .ofKotlinClass<Planet>()
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
        .returning(materialGenerator(), minimumInstances = 0, maximumInstances = 3)
        .build()

    private fun materialGenerator(): RandomGen<Material> = RandomGenBuilder<Material>()
        .ofKotlinClass<Material>()
        .withField("compounds", ProvidingMethod.Constructor)
        .returning(
            listOf(
                materialAr,
                materialCH4,
                materialCO2,
                materialH2,
                materialHe,
                materialN2,
                materialO2
            )
        )
        .build()
}
