package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider

import java.util.Random

/**
 * A [FieldDataProvider] that generates a [Float] value.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class FloatFieldDataProvider<OUTPUT_TYPE>
/**
 * Returns a new instance of [FloatFieldDataProvider] generating a [Float] between [minimum] and [maximum].
 *
 * @param random A random value generator
 * @param minimum The lowest possible value (default: `0f`)
 * @param maximum The highest possible value (default: `1f`)
 */
constructor(
    private val random: Random,
    private val minimum: Float = 0f,
    private val maximum: Float = 1f
) : FieldDataProvider<OUTPUT_TYPE, Float>() {
    override fun invoke(instance: OUTPUT_TYPE?) = random.nextFloat() * (maximum - minimum) + minimum
}
