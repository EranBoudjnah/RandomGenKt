package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.ArrayList
import java.util.Random

/**
 * Created by Eran Boudjnah on 25/04/2018.
 */
class CustomListRangeFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>(
    private val random: Random,
    private val minInstances: Int,
    private val maxInstances: Int,
    private val fieldDataProvider: (OUTPUT_TYPE?) -> VALUE_TYPE
) : FieldDataProvider<OUTPUT_TYPE, List<VALUE_TYPE>>() {
    override fun invoke(instance: OUTPUT_TYPE?): List<VALUE_TYPE> {
        val instances by lazy { random.nextInt(maxInstances - minInstances + 1) + minInstances }
        val ret by lazy { ArrayList<VALUE_TYPE>(instances) }

        repeat(instances) {
            ret.add(fieldDataProvider(instance))
        }

        return ret
    }
}
