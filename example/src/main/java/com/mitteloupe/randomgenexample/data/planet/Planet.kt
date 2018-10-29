package com.mitteloupe.randomgenexample.data.planet

/**
 * Created by Eran Boudjnah on 24/04/2018.
 */
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