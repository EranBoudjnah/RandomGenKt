package com.mitteloupe.randomgen.fielddataprovider

import com.mitteloupe.randomgen.FieldDataProvider

/**
 * A [FieldDataProvider] that generates an explicit value.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class ExplicitFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>
/**
 * Creates an instance of [ExplicitFieldDataProvider] generating the same explicit value every time.
 *
 * @param value The value to generate
 */
constructor(
	private val value: VALUE_TYPE
) : FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE> {

	override fun generate(instance: OUTPUT_TYPE?): VALUE_TYPE {
		return value
	}
}
