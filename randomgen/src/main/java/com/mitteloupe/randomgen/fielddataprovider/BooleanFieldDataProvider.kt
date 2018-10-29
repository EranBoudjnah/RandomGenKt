package com.mitteloupe.randomgen.fielddataprovider

import com.mitteloupe.randomgen.FieldDataProvider

import java.util.Random

/**
 * A [FieldDataProvider] that generates a random [Boolean] value.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class BooleanFieldDataProvider<OUTPUT_TYPE>(
	private val random: Random
) : FieldDataProvider<OUTPUT_TYPE, Boolean> {
	override fun generate(instance: OUTPUT_TYPE?): Boolean {
		return random.nextBoolean()
	}
}
