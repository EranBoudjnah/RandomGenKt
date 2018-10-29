package com.mitteloupe.randomgen

import java.util.Date

/**
 * Created by Eran Boudjnah on 07/08/2018.
 */
interface FieldDataProviderFactory<OUTPUT_TYPE> {
	val booleanFieldDataProvider: FieldDataProvider<OUTPUT_TYPE, Boolean>

	val byteFieldDataProvider: FieldDataProvider<OUTPUT_TYPE, Byte>

	val sequentialIntegerFieldDataProvider: FieldDataProvider<OUTPUT_TYPE, Int>

	val uuidFieldDataProvider: FieldDataProvider<OUTPUT_TYPE, String>

	val dateFieldDataProvider: FieldDataProvider<OUTPUT_TYPE, Date>

	val loremIpsumFieldDataProvider: FieldDataProvider<OUTPUT_TYPE, String>
	fun <VALUE_TYPE> getExplicitFieldDataProvider(value: VALUE_TYPE): FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>

	fun <VALUE_TYPE> getGenericListFieldDataProvider(immutableList: List<VALUE_TYPE>): FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>

	fun getByteListFieldDataProvider(size: Int): FieldDataProvider<OUTPUT_TYPE, List<Byte>>

	fun getByteListFieldDataProvider(minSize: Int, maxSize: Int): FieldDataProvider<OUTPUT_TYPE, List<Byte>>

	fun getDoubleFieldDataProvider(minimum: Double = 0.0, maximum: Double = 1.0): FieldDataProvider<OUTPUT_TYPE, Double>

	fun getFloatFieldDataProvider(minimum: Float = 0.0f, maximum: Float = 1.0f): FieldDataProvider<OUTPUT_TYPE, Float>

	fun getIntFieldDataProvider(minimum: Int = Int.MIN_VALUE, maximum: Int = Int.MAX_VALUE): FieldDataProvider<OUTPUT_TYPE, Int>

	fun getLongFieldDataProvider(minimum: Long = Long.MIN_VALUE, maximum: Long = Long.MAX_VALUE): FieldDataProvider<OUTPUT_TYPE, Long>

	fun getSequentialIntegerFieldDataProvider(startValue: Int): FieldDataProvider<OUTPUT_TYPE, Int>

	fun getRgbFieldDataProvider(provideAlpha: Boolean): FieldDataProvider<OUTPUT_TYPE, String>

	fun getDateFieldDataProvider(earliestTimestamp: Long = 0L, latestTimestamp: Long = Long.MAX_VALUE): FieldDataProvider<OUTPUT_TYPE, Date>

	fun getLoremIpsumFieldDataProvider(length: Int? = null): FieldDataProvider<OUTPUT_TYPE, String>

	fun getLoremIpsumFieldDataProvider(minLength: Int, maxLength: Int, paragraphDelimiter: String? = null): FieldDataProvider<OUTPUT_TYPE, String>

	fun getWeightedFieldDataProvidersFieldDataProvider(fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, *>): FieldDataProvider<OUTPUT_TYPE, *>

	fun <ENUM_TYPE : Enum<*>> getRandomEnumFieldDataProvider(value: Class<ENUM_TYPE>): FieldDataProvider<OUTPUT_TYPE, ENUM_TYPE>

	fun getPaddedFieldDataProvider(minimumLength: Int, paddingString: String, fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, Any>): FieldDataProvider<OUTPUT_TYPE, String>

	fun <VALUE_TYPE> getCustomListFieldDataProvider(instances: Int, fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>): FieldDataProvider<OUTPUT_TYPE, List<VALUE_TYPE>>

	fun <VALUE_TYPE> getCustomListRangeFieldDataProvider(minInstances: Int, maxInstances: Int, fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>): FieldDataProvider<OUTPUT_TYPE, List<VALUE_TYPE>>
}
