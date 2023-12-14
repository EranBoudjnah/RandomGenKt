package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

class DoubleFieldDataProvider<OUTPUT_TYPE>(
    private val random: Random,
    private val minimum: Double = 0.0,
    private val maximum: Double = 1.0
) : FieldDataProvider<OUTPUT_TYPE, Double>() {
    override fun invoke(instance: OUTPUT_TYPE?) =
        random.nextDouble() * (maximum - minimum) + minimum
}
