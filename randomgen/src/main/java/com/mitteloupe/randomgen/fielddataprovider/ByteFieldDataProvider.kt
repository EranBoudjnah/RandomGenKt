package com.mitteloupe.randomgen.fielddataprovider

import com.mitteloupe.randomgen.FieldDataProvider

import java.util.Random

/**
 * A [FieldDataProvider] that generates a single [Byte] value.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class ByteFieldDataProvider<OUTPUT_TYPE>(
	private val random: Random
) : FieldDataProvider<OUTPUT_TYPE, Byte> {
	private val byteArray = ByteArray(1)

	override fun generate(instance: OUTPUT_TYPE?): Byte {
		random.nextBytes(byteArray)
		return byteArray[0]
	}
}
