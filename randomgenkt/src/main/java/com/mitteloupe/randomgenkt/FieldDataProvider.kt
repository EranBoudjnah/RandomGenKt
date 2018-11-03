package com.mitteloupe.randomgenkt

/**
 * Generates one value of the defined type.
 */
abstract class FieldDataProvider<in OUTPUT_TYPE, out FIELD_DATA_TYPE> : (OUTPUT_TYPE?) -> FIELD_DATA_TYPE {
	fun invoke(): FIELD_DATA_TYPE = invoke(null)
}
