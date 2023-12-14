package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

class GenericListFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE : Any>(
    private val random: Random,
    private val list: List<VALUE_TYPE>
) : FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>() {
    override fun invoke(instance: OUTPUT_TYPE?) = list[getNextIndex()]

    private fun getNextIndex() = random.nextInt(list.size)
}
