package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

class IntFieldDataProvider<OUTPUT_TYPE>(
    private val random: Random,
    private val minimum: Int = Int.MIN_VALUE,
    private val maximum: Int = Int.MAX_VALUE
) : FieldDataProvider<OUTPUT_TYPE, Int>() {
    override fun invoke(instance: OUTPUT_TYPE?): Int {
        val minimumBigDecimal = minimum.toBigDecimal()
        val maximumBigDecimal = maximum.toBigDecimal()
        val value = maximumBigDecimal
            .subtract(minimumBigDecimal)
            .add(1.toBigDecimal())
            .multiply(random.nextDouble().toBigDecimal())
            .add(minimumBigDecimal)

        return value.toInt()
    }
}
