package com.mitteloupe.randomgenkt

import java.util.Date

/**
 * Created by Eran Boudjnah on 07/08/2018.
 */
interface FieldDataProviderFactory<OUTPUT_TYPE> {
	val booleanFieldDataProvider: (instance: OUTPUT_TYPE?) -> Boolean

	val byteFieldDataProvider: (instance: OUTPUT_TYPE?) -> Byte

	val sequentialIntegerFieldDataProvider: (instance: OUTPUT_TYPE?) -> Int

	val uuidFieldDataProvider: (instance: OUTPUT_TYPE?) -> String

	val dateFieldDataProvider: (instance: OUTPUT_TYPE?) -> Date

	val loremIpsumFieldDataProvider: (instance: OUTPUT_TYPE?) -> String

	fun <VALUE_TYPE> getExplicitFieldDataProvider(value: VALUE_TYPE): (instance: OUTPUT_TYPE?) -> VALUE_TYPE

	fun <VALUE_TYPE> getGenericListFieldDataProvider(immutableList: List<VALUE_TYPE>): (instance: OUTPUT_TYPE?) -> VALUE_TYPE

	fun getByteListFieldDataProvider(size: Int): (instance: OUTPUT_TYPE?) -> List<Byte>

	fun getByteListFieldDataProvider(minSize: Int, maxSize: Int): (instance: OUTPUT_TYPE?) -> List<Byte>

	fun getDoubleFieldDataProvider(minimum: Double = 0.0, maximum: Double = 1.0): (instance: OUTPUT_TYPE?) -> Double

	fun getFloatFieldDataProvider(minimum: Float = 0.0f, maximum: Float = 1.0f): (instance: OUTPUT_TYPE?) -> Float

	fun getIntFieldDataProvider(minimum: Int = Int.MIN_VALUE, maximum: Int = Int.MAX_VALUE): (instance: OUTPUT_TYPE?) -> Int

	fun getLongFieldDataProvider(minimum: Long = Long.MIN_VALUE, maximum: Long = Long.MAX_VALUE): (instance: OUTPUT_TYPE?) -> Long

	fun getSequentialIntegerFieldDataProvider(startValue: Int): (instance: OUTPUT_TYPE?) -> Int

	fun getRgbFieldDataProvider(provideAlpha: Boolean): (instance: OUTPUT_TYPE?) -> String

	fun getDateFieldDataProvider(earliestTimestamp: Long = 0L, latestTimestamp: Long = Long.MAX_VALUE): (instance: OUTPUT_TYPE?) -> Date

	fun getLoremIpsumFieldDataProvider(length: Int? = null): (instance: OUTPUT_TYPE?) -> String

	fun getLoremIpsumFieldDataProvider(minLength: Int, maxLength: Int, paragraphDelimiter: String? = null): (instance: OUTPUT_TYPE?) -> String

	fun getWeightedFieldDataProvidersFieldDataProvider(fieldDataProvider: (instance: OUTPUT_TYPE?) -> Any): (instance: OUTPUT_TYPE?) -> Any

	fun <ENUM_TYPE : Enum<*>> getRandomEnumFieldDataProvider(value: Class<ENUM_TYPE>): (instance: OUTPUT_TYPE?) -> ENUM_TYPE

	fun getPaddedFieldDataProvider(minimumLength: Int, paddingString: String, fieldDataProvider: (instance: OUTPUT_TYPE?) -> Any): (instance: OUTPUT_TYPE?) -> String

	fun <VALUE_TYPE> getCustomListFieldDataProvider(instances: Int, fieldDataProvider: (instance: OUTPUT_TYPE?) -> VALUE_TYPE): (instance: OUTPUT_TYPE?) -> List<VALUE_TYPE>

	fun <VALUE_TYPE> getCustomListRangeFieldDataProvider(minInstances: Int, maxInstances: Int, fieldDataProvider: (instance: OUTPUT_TYPE?) -> VALUE_TYPE): (instance: OUTPUT_TYPE?) -> List<VALUE_TYPE>
}
