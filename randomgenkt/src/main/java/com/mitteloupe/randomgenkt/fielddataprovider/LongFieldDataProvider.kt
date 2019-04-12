package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

/**
 * A [FieldDataProvider] that generates a [Long] value.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class LongFieldDataProvider<OUTPUT_TYPE>
/**
 * Returns a new instance of [LongFieldDataProvider] generating a [Long] between [minimum] and [maximum].
 *
 * @param random A random value generator
 * @param minimum The lowest possible value
 * @param maximum The highest possible value
 */
constructor(
    private val random: Random,
    private val minimum: Long = Long.MIN_VALUE,
    private val maximum: Long = Long.MAX_VALUE
) : FieldDataProvider<OUTPUT_TYPE, Long>() {
    override fun invoke(instance: OUTPUT_TYPE?): Long {
        val minBigDecimal = minimum.toBigDecimal()
        val maxBigDecimal = maximum.toBigDecimal()
        val value = maxBigDecimal
            .subtract(minBigDecimal)
            .add(1.toBigDecimal())
            .multiply(random.nextDouble().toBigDecimal())
            .add(minBigDecimal)

        return value.toLong()
    }
}
