package com.mitteloupe.randomgen.fielddataprovider

import com.mitteloupe.randomgen.FieldDataProvider
import java.util.Random

/**
 * A [FieldDataProvider] that generates an [Int] value.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class IntFieldDataProvider<OUTPUT_TYPE>
/**
 * Returns a new instance of [IntFieldDataProvider] generating an [Int] between [minimum] and [maximum].
 *
 * @param random  A random value generator
 * @param minimum The lowest possible value (default: [Int.MIN_VALUE])
 * @param maximum The highest possible value (default: [Int.MAX_VALUE])
 */
constructor(
	private val random: Random,
	private val minimum: Int = Int.MIN_VALUE,
	private val maximum: Int = Int.MAX_VALUE
) : FieldDataProvider<OUTPUT_TYPE, Int> {

	override fun generate(instance: OUTPUT_TYPE?): Int {
		val minBigDecimal = minimum.toBigDecimal()
		val maxBigDecimal = maximum.toBigDecimal()
		val value = maxBigDecimal
			.subtract(minBigDecimal)
			.add(1.toBigDecimal())
			.multiply(random.nextDouble().toBigDecimal())
			.add(minBigDecimal)
		return value.toInt()
	}
}
