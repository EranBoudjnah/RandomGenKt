package com.mitteloupe.randomgenkt

import com.mitteloupe.randomgenkt.fielddataprovider.ShortFieldDataProvider
import java.util.Date
import kotlin.reflect.KClass

interface FieldDataProviderFactory<OUTPUT_TYPE> {
    val booleanFieldDataProvider: FieldDataProvider<OUTPUT_TYPE, Boolean>

    val byteFieldDataProvider: FieldDataProvider<OUTPUT_TYPE, Byte>

    val sequentialIntegerFieldDataProvider: FieldDataProvider<OUTPUT_TYPE, Int>

    val uuidFieldDataProvider: FieldDataProvider<OUTPUT_TYPE, String>

    val dateFieldDataProvider: FieldDataProvider<OUTPUT_TYPE, Date>

    val loremIpsumFieldDataProvider: FieldDataProvider<OUTPUT_TYPE, String>

    fun <VALUE_TYPE : Any> getExplicitFieldDataProvider(
        value: VALUE_TYPE
    ): FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>

    fun <VALUE_TYPE : Any> getGenericListFieldDataProvider(
        fieldValueTypes: List<VALUE_TYPE>
    ): FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>

    fun getByteArrayFieldDataProvider(size: Int): FieldDataProvider<OUTPUT_TYPE, ByteArray>

    fun getByteArrayFieldDataProvider(
        minimumSize: Int,
        maximumSize: Int
    ): FieldDataProvider<OUTPUT_TYPE, ByteArray>

    fun getDoubleFieldDataProvider(
        minimum: Double = 0.0,
        maximum: Double = 1.0
    ): FieldDataProvider<OUTPUT_TYPE, Double>

    fun getFloatFieldDataProvider(
        minimum: Float = 0.0f,
        maximum: Float = 1.0f
    ): FieldDataProvider<OUTPUT_TYPE, Float>

    fun getIntFieldDataProvider(
        minimum: Int = Int.MIN_VALUE,
        maximum: Int = Int.MAX_VALUE
    ): FieldDataProvider<OUTPUT_TYPE, Int>

    fun getLongFieldDataProvider(
        minimum: Long = Long.MIN_VALUE,
        maximum: Long = Long.MAX_VALUE
    ): FieldDataProvider<OUTPUT_TYPE, Long>

    fun getShortFieldDataProvider(
        minimum: Short = Short.MIN_VALUE,
        maximum: Short = Short.MAX_VALUE
    ): ShortFieldDataProvider<OUTPUT_TYPE>

    fun getSequentialIntegerFieldDataProvider(startValue: Int): FieldDataProvider<OUTPUT_TYPE, Int>

    fun getRgbFieldDataProvider(provideAlpha: Boolean): FieldDataProvider<OUTPUT_TYPE, String>

    fun getDateFieldDataProvider(
        earliestTimestamp: Long = 0L,
        latestTimestamp: Long = Long.MAX_VALUE
    ): FieldDataProvider<OUTPUT_TYPE, Date>

    fun getLoremIpsumFieldDataProvider(
        minimumLength: Int,
        maximumLength: Int = minimumLength,
        paragraphDelimiter: String? = null
    ): FieldDataProvider<OUTPUT_TYPE, String>

    fun getWeightedFieldDataProvidersFieldDataProvider(
        fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, Any>
    ): FieldDataProvider<OUTPUT_TYPE, Any>

    fun <ENUM_TYPE : Enum<*>> getRandomEnumFieldDataProvider(
        value: Class<ENUM_TYPE>
    ): FieldDataProvider<OUTPUT_TYPE, ENUM_TYPE>

    fun <ENUM_TYPE : Enum<*>> getRandomEnumFieldDataProvider(
        value: KClass<ENUM_TYPE>
    ): FieldDataProvider<OUTPUT_TYPE, ENUM_TYPE>

    fun getPaddedFieldDataProvider(
        fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, Any>,
        minimumLength: Int,
        paddingString: String
    ): FieldDataProvider<OUTPUT_TYPE, String>

    fun <VALUE_TYPE : Any> getCustomListFieldDataProvider(
        fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>,
        minimumInstances: Int,
        maximumInstances: Int = minimumInstances
    ): FieldDataProvider<OUTPUT_TYPE, List<VALUE_TYPE>>
}
