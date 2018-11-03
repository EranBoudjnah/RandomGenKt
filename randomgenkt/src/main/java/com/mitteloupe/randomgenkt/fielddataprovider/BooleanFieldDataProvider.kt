package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider

import java.util.Random

/**
 * A [FieldDataProvider] that generates a random [Boolean] value.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class BooleanFieldDataProvider<OUTPUT_TYPE>(
	private val random: Random
) : FieldDataProvider<OUTPUT_TYPE, Boolean>() {
	override fun invoke(instance: OUTPUT_TYPE?) = random.nextBoolean()
}
