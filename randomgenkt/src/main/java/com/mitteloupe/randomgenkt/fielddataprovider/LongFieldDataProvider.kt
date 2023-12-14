package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

class LongFieldDataProvider<OUTPUT_TYPE>(
    private val random: Random,
    private val minimum: Long = Long.MIN_VALUE,
    private val maximum: Long = Long.MAX_VALUE
) : FieldDataProvider<OUTPUT_TYPE, Long>() {
    override fun invoke(instance: OUTPUT_TYPE?): Long {
        val minBigDecimal = minimum.toBigDecimal()
        val maxBigDecimal = maximum.toBigDecimal()
        val randomValue =
            ("0." + (random.nextDouble().paddedPrecision() + random.nextDouble().paddedPrecision()))
                .toBigDecimal()
                .stripTrailingZeros()
        val value = maxBigDecimal
            .subtract(minBigDecimal)
            .add(1.toBigDecimal())
            .multiply(randomValue)
            .add(minBigDecimal)

        return value.toLong()
    }

    private fun Double.paddedPrecision() = if (this == 0.0) {
        "0".repeat(17)
    } else {
        val result = toString()
            .substringAfter(".")
            .take(16)
            .padEnd(16, '0')
        result
    }
}
