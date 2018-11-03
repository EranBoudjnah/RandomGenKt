package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

/**
 * Created by Eran Boudjnah on 24/04/2018.
 */
class GenericListFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>(
	private val random: Random,
	private val list: List<VALUE_TYPE>
) : FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>() {
	private val immutableList = list.toList()

	override fun invoke(instance: OUTPUT_TYPE?) = immutableList[(random.nextDouble() * list.size).toInt()]
}
