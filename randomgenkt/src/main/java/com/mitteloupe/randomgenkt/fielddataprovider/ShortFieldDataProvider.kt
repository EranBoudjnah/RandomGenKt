package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

/**
 * A [FieldDataProvider] that generates an [Short] value.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class ShortFieldDataProvider<OUTPUT_TYPE>
/**
 * Returns a new instance of [ShortFieldDataProvider] generating a [Short] between [minimum] and [maximum].
 *
 * @param random A random value generator
 * @param minimum The lowest possible value (default: [Short.MIN_VALUE])
 * @param maximum The highest possible value (default: [Short.MAX_VALUE])
 */
constructor(
    private val random: Random,
    private val minimum: Short = Short.MIN_VALUE,
    private val maximum: Short = Short.MAX_VALUE
) : FieldDataProvider<OUTPUT_TYPE, Short>() {
    override fun invoke(instance: OUTPUT_TYPE?): Short {
        val minBigDecimal = minimum.toInt().toBigDecimal()
        val maxBigDecimal = maximum.toInt().toBigDecimal()
        val value = maxBigDecimal
            .subtract(minBigDecimal)
            .add(1.toBigDecimal())
            .multiply(random.nextDouble().toBigDecimal())
            .add(minBigDecimal)

        return value.toShort()
    }
}
