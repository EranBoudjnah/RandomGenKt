package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider

import java.util.Random

/**
 * A [FieldDataProvider] that generates a `Double` value.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class DoubleFieldDataProvider<OUTPUT_TYPE>
private constructor(
    private val random: Random,
    private val minimum: Double = 0.0,
    private val maximum: Double = 1.0
) : FieldDataProvider<OUTPUT_TYPE, Double>() {
    override fun invoke(instance: OUTPUT_TYPE?) = random.nextDouble() * (maximum - minimum) + minimum

    companion object {

        /**
		 * Returns a new instance of [DoubleFieldDataProvider] generating a [Double] between `0.0` and `1.0`.
		 *
		 * @param random A random value generator
		 */
        fun <OUTPUT_TYPE> getInstance(random: Random) = DoubleFieldDataProvider<OUTPUT_TYPE>(random)

        /**
		 * Returns a new instance of [DoubleFieldDataProvider] generating a [Double] between [minimum] and [maximum].
		 *
		 * @param random A random value generator
		 * @param minimum The lowest possible value
		 * @param maximum The highest possible value
		 */
        fun <OUTPUT_TYPE> getInstanceWithRange(random: Random, minimum: Double, maximum: Double) =
            DoubleFieldDataProvider<OUTPUT_TYPE>(random, minimum, maximum)
    }
}
