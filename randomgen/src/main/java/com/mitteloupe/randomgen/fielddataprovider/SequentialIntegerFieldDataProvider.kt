package com.mitteloupe.randomgen.fielddataprovider

import com.mitteloupe.randomgen.FieldDataProvider

/**
 * A [FieldDataProvider] that generates sequential [Integer] values.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class SequentialIntegerFieldDataProvider<OUTPUT_TYPE>
/**
 * Returns a new instance of [SequentialIntegerFieldDataProvider] generating sequential [Integer] values, starting at [counter], inclusive.
 *
 * @param counter The first value to generate. (Default: `1`)
 */
constructor(private var counter: Int = 1) : FieldDataProvider<OUTPUT_TYPE, Int> {
	override fun generate(instance: OUTPUT_TYPE?): Int {
		return counter++
	}
}
