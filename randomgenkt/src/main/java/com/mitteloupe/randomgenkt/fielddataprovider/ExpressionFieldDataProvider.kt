package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider

class ExpressionFieldDataProvider<INSTANCE, FIELD_DATA : Any>(
    private val expression: (INSTANCE?) -> FIELD_DATA
) : FieldDataProvider<INSTANCE, FIELD_DATA>() {
    override fun invoke(instance: INSTANCE?): FIELD_DATA = expression(instance)
}
