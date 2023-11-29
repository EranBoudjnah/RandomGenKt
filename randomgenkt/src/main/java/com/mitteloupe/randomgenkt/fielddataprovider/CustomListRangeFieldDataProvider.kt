package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

class CustomListRangeFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>(
    private val random: Random,
    private val minInstances: Int,
    private val maxInstances: Int,
    private val fieldDataProvider: (OUTPUT_TYPE?) -> VALUE_TYPE
) : FieldDataProvider<OUTPUT_TYPE, List<VALUE_TYPE>>() {
    override fun invoke(instance: OUTPUT_TYPE?): List<VALUE_TYPE> {
        val instances = random.nextInt(maxInstances - minInstances + 1) + minInstances
        return List(instances) {
            fieldDataProvider(instance)
        }
    }
}
