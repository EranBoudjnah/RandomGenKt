package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.ArrayList

class CustomListFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>(
    private val instances: Int,
    private val fieldDataProvider: (OUTPUT_TYPE?) -> VALUE_TYPE
) : FieldDataProvider<OUTPUT_TYPE, List<VALUE_TYPE>>() {
    override fun invoke(instance: OUTPUT_TYPE?): List<VALUE_TYPE> {
        val ret by lazy { ArrayList<VALUE_TYPE>(instances) }

        repeat(instances) {
            ret.add(fieldDataProvider(instance))
        }

        return ret
    }
}
