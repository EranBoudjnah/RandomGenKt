package com.mitteloupe.randomgen.fielddataprovider

import com.mitteloupe.randomgen.FieldDataProvider

import java.util.ArrayList

/**
 * A [FieldDataProvider] that generates a fixed sized list of instances of type [VALUE_TYPE].
 *
 * Created by Eran Boudjnah on 25/04/2018.
 */
class CustomListFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>(
	private val instances: Int,
	private val fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>
) : FieldDataProvider<OUTPUT_TYPE, List<VALUE_TYPE>> {

	override fun generate(instance: OUTPUT_TYPE?): List<VALUE_TYPE> {
		val ret = ArrayList<VALUE_TYPE>(instances)
		repeat(instances) {
			ret.add(fieldDataProvider.generate(instance))
		}
		return ret
	}
}
