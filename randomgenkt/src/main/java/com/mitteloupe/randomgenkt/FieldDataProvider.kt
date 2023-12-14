package com.mitteloupe.randomgenkt

/**
 * Generates one value of the defined type.
 */
abstract class FieldDataProvider<in INSTANCE, out FIELD_DATA : Any> : (INSTANCE?) -> FIELD_DATA {
    operator fun invoke(): FIELD_DATA = invoke(null)
}
