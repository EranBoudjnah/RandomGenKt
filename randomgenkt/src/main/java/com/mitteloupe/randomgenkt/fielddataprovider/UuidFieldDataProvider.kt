package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import com.mitteloupe.randomgenkt.UuidGenerator

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
) : FieldDataProvider<OUTPUT_TYPE, String>() {
	override fun invoke(instance: OUTPUT_TYPE?) = uuidGenerator.randomUUID()
}
