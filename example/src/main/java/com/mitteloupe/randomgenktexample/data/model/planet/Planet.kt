package com.mitteloupe.randomgenktexample.data.model.planet

data class Planet(
    val id: Int,
    val diameterEarthRatio: Float,
    val solarMass: Float,
    val orbitalPeriodYears: Float,
    val rotationPeriodDays: Float,
    val moons: Int,
    val hasRings: Boolean,
    val atmosphere: List<Material>
)
