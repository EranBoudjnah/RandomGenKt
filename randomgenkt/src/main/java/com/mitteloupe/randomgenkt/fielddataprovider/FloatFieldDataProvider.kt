package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

class FloatFieldDataProvider<OUTPUT_TYPE>(
    private val random: Random,
    private val minimum: Float = 0f,
    private val maximum: Float = 1f
) : FieldDataProvider<OUTPUT_TYPE, Float>() {
    override fun invoke(instance: OUTPUT_TYPE?) = random.nextFloat() * (maximum - minimum) + minimum
}
