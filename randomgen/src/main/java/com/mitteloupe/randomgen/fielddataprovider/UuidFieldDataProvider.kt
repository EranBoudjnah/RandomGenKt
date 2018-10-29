package com.mitteloupe.randomgen.fielddataprovider

import com.mitteloupe.randomgen.FieldDataProvider
import com.mitteloupe.randomgen.UuidGenerator

/**
 * A [FieldDataProvider] that generates random `UUID` `String` values.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class UuidFieldDataProvider<OUTPUT_TYPE>
/**
 * Creates an instance of [UuidFieldDataProvider] generating random `UUID` `String` values.
 *
 * @param uuidGenerator A generator of UUIDs
 */
constructor(
	private val uuidGenerator: UuidGenerator
) : FieldDataProvider<OUTPUT_TYPE, String> {

	override fun generate(instance: OUTPUT_TYPE?): String {
		return uuidGenerator.randomUUID()
	}
}
