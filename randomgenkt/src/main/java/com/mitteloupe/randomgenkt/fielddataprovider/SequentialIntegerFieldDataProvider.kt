package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider

class SequentialIntegerFieldDataProvider<OUTPUT_TYPE>(
    initialCounterValue: Int = 1
) : FieldDataProvider<OUTPUT_TYPE, Int>() {
    private var counter: Int = initialCounterValue

    override fun invoke(instance: OUTPUT_TYPE?) = counter++
}
