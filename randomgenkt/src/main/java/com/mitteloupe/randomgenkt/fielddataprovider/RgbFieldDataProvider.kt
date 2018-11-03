package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider

import java.util.Random

/**
 * A [FieldDataProvider] that generates an RGB/RGBA [String] value.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class RgbFieldDataProvider<OUTPUT_TYPE>
/**
 * Creates an instance of [RgbFieldDataProvider] generating a random RGB or RGBA [String] value.
 *
 * Values are in the format `#RRGGBB` or `#AARRGGBB`, respectively.
 *
 * @param random       A random value generator
 * @param provideAlpha True to generate RGBA values, false to generate RGB values
 */
constructor(
	private val random: Random,
	private val provideAlpha: Boolean
) : FieldDataProvider<OUTPUT_TYPE, String>() {
	override fun invoke(instance: OUTPUT_TYPE?): String {
		val red = random.nextInt(255)
		val green = random.nextInt(255)
		val blue = random.nextInt(255)
		return if (provideAlpha) {
			val alpha = random.nextInt(255)
			String.format("#%02x%02x%02x%02x", alpha, red, green, blue)

		} else {
			String.format("#%02x%02x%02x", red, green, blue)
		}
	}
}
