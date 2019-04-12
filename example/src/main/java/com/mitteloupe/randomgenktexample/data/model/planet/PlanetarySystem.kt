package com.mitteloupe.randomgenktexample.data.model.planet

/**
 * Created by Eran Boudjnah on 25/04/2018.
 */
data class PlanetarySystem(
    val id: Int,
    val starAgeBillionYears: Float,
    val starDiameterSunRadii: Double,
    val starSolarMass: Double,
    val planets: Array<Planet>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlanetarySystem

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}
