package com.mitteloupe.randomgenkt.datasource

import java.util.Arrays

/**
 * Created by Eran Boudjnah on 29/04/2018.
 */
@Suppress("unused")
class ColorsDataSource private constructor() {

	val colors: List<String>
		get() = listOf("Black", "Blue", "Brown", "Cyan", "Green", "Grey", "Magenta", "Orange", "Pink", "Red", "Violet", "White", "Yellow")

	companion object {
		val instance = ColorsDataSource()
	}
}
