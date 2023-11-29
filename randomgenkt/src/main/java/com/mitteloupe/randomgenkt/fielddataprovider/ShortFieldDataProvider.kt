package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

class ShortFieldDataProvider<OUTPUT_TYPE>(
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
