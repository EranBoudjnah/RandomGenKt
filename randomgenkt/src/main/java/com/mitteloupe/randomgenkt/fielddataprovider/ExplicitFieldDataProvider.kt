package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider

class ExplicitFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE : Any>(
    private val value: VALUE_TYPE
) : FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>() {
    override fun invoke(instance: OUTPUT_TYPE?) = value
}
