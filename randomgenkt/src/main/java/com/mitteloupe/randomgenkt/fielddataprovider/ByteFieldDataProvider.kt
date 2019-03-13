package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider

import java.util.Random

/**
 * A [FieldDataProvider] that generates a single [Byte] value.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class ByteFieldDataProvider<OUTPUT_TYPE>(
	private val random: Random
) : FieldDataProvider<OUTPUT_TYPE, Byte>() {
	private val byteArray by lazy { ByteArray(1) }

	override fun invoke(instance: OUTPUT_TYPE?): Byte {
		random.nextBytes(byteArray)
		return byteArray[0]
	}
}
