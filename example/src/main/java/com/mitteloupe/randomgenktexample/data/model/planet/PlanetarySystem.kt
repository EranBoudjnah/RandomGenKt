package com.mitteloupe.randomgenktexample.data.model.planet

data class PlanetarySystem(
    val id: Int,
    val starAgeBillionYears: Float,
    val starDiameterSunRadii: Double,
    val starSolarMass: Double,
    val planets: List<Planet>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlanetarySystem

        return id == other.id
    }

    override fun hashCode(): Int = id
}
