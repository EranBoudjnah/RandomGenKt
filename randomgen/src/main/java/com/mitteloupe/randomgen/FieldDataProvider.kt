package com.mitteloupe.randomgen

/**
 * Generates one value of the defined type.
 */
interface FieldDataProvider<in OUTPUT_TYPE, out FIELD_DATA_TYPE> {
	fun generate(instance: OUTPUT_TYPE? = null): FIELD_DATA_TYPE
}
