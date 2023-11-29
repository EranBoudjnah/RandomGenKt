package com.mitteloupe.randomgenkt.datasource

@Suppress("unused")
class ColorsDataSource private constructor() {
    val colors: List<String>
        by lazy {
            listOf(
                "Black",
                "Blue",
                "Brown",
                "Cyan",
                "Green",
                "Grey",
                "Magenta",
                "Orange",
                "Pink",
                "Red",
                "Violet",
                "White",
                "Yellow"
            )
        }

    companion object {
        val instance = ColorsDataSource()
    }
}
