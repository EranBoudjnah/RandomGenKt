package com.mitteloupe.randomgen.fielddataprovider

import com.mitteloupe.randomgen.FieldDataProvider
import java.util.Random

/**
 * Created by Eran Boudjnah on 24/04/2018.
 */
class GenericListFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>(
	private val random: Random,
	private val list: List<VALUE_TYPE>
) : FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE> {
	private val immutableList: List<VALUE_TYPE> = list.toList()

	override fun generate(instance: OUTPUT_TYPE?): VALUE_TYPE {
		return immutableList[(random.nextDouble() * list.size).toInt()]
	}
}
