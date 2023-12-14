package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

class BooleanFieldDataProvider<OUTPUT_TYPE>(
    private val random: Random
) : FieldDataProvider<OUTPUT_TYPE, Boolean>() {
    override fun invoke(instance: OUTPUT_TYPE?) = random.nextBoolean()
}
