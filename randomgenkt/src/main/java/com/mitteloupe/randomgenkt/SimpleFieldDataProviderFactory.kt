package com.mitteloupe.randomgenkt

import com.mitteloupe.randomgenkt.fielddataprovider.BooleanFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.ByteFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.ByteListFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.CustomListFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.CustomListRangeFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.DateFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.DoubleFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.ExplicitFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.FloatFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.GenericListFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.IntFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.LongFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.LoremIpsumFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.PaddedFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.RandomEnumFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.RgbFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.SequentialIntegerFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.UuidFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.WeightedFieldDataProvidersFieldDataProvider
import java.util.Random

/**
 * Created by Eran Boudjnah on 07/08/2018.
 */
internal open class SimpleFieldDataProviderFactory<OUTPUT_TYPE>(
    private val random: Random,
    private val uuidGenerator: UuidGenerator
) : FieldDataProviderFactory<OUTPUT_TYPE> {

    override val booleanFieldDataProvider: BooleanFieldDataProvider<OUTPUT_TYPE>
        get() = BooleanFieldDataProvider(random)

    override val byteFieldDataProvider: ByteFieldDataProvider<OUTPUT_TYPE>
        get() = ByteFieldDataProvider(random)

    override val sequentialIntegerFieldDataProvider: SequentialIntegerFieldDataProvider<OUTPUT_TYPE>
        get() = SequentialIntegerFieldDataProvider()

    override val uuidFieldDataProvider: UuidFieldDataProvider<OUTPUT_TYPE>
        get() = UuidFieldDataProvider(uuidGenerator)

    override val dateFieldDataProvider: DateFieldDataProvider<OUTPUT_TYPE>
        get() = DateFieldDataProvider(random)

    override val loremIpsumFieldDataProvider: LoremIpsumFieldDataProvider<OUTPUT_TYPE>
        get() = LoremIpsumFieldDataProvider.getInstance()

    override fun <VALUE_TYPE> getExplicitFieldDataProvider(value: VALUE_TYPE) =
        ExplicitFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>(value)

    override fun <VALUE_TYPE> getGenericListFieldDataProvider(immutableList: List<VALUE_TYPE>) =
        GenericListFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>(random, immutableList)

    override fun getByteListFieldDataProvider(size: Int) =
        ByteListFieldDataProvider.getInstanceWithSize<OUTPUT_TYPE>(random, size)

    override fun getByteListFieldDataProvider(minSize: Int, maxSize: Int) =
        ByteListFieldDataProvider.getInstanceWithSizeRange<OUTPUT_TYPE>(random, minSize, maxSize)

    override fun getDoubleFieldDataProvider(minimum: Double, maximum: Double) =
        DoubleFieldDataProvider.getInstanceWithRange<OUTPUT_TYPE>(random, minimum, maximum)

    override fun getFloatFieldDataProvider(minimum: Float, maximum: Float) =
        FloatFieldDataProvider<OUTPUT_TYPE>(random, minimum, maximum)

    override fun getIntFieldDataProvider(minimum: Int, maximum: Int) =
        IntFieldDataProvider<OUTPUT_TYPE>(random, minimum, maximum)

    override fun getLongFieldDataProvider(minimum: Long, maximum: Long) =
        LongFieldDataProvider<OUTPUT_TYPE>(random, minimum, maximum)

    override fun getSequentialIntegerFieldDataProvider(startValue: Int) =
        SequentialIntegerFieldDataProvider<OUTPUT_TYPE>(startValue)

    override fun getRgbFieldDataProvider(provideAlpha: Boolean) =
        RgbFieldDataProvider<OUTPUT_TYPE>(random, provideAlpha)

    override fun getDateFieldDataProvider(earliestTimestamp: Long, latestTimestamp: Long) =
        DateFieldDataProvider<OUTPUT_TYPE>(random, earliestTimestamp, latestTimestamp)

    override fun getLoremIpsumFieldDataProvider(length: Int?) =
        when (length) {
            null -> LoremIpsumFieldDataProvider.getInstance()
            else -> LoremIpsumFieldDataProvider.getInstance<OUTPUT_TYPE>(length)
        }

    override fun getLoremIpsumFieldDataProvider(minLength: Int, maxLength: Int, paragraphDelimiter: String?) =
        when (paragraphDelimiter) {
            null -> LoremIpsumFieldDataProvider.getInstanceWithRange(random, minLength, maxLength)
            else -> LoremIpsumFieldDataProvider.getInstanceWithRange<OUTPUT_TYPE>(random, minLength, maxLength, paragraphDelimiter)
        }

    override fun getWeightedFieldDataProvidersFieldDataProvider(fieldDataProvider: (instance: OUTPUT_TYPE?) -> Any) =
        WeightedFieldDataProvidersFieldDataProvider(random, fieldDataProvider)

    override fun <ENUM_TYPE : Enum<*>> getRandomEnumFieldDataProvider(value: Class<ENUM_TYPE>) =
        RandomEnumFieldDataProvider<OUTPUT_TYPE, ENUM_TYPE>(random, value)

    override fun getPaddedFieldDataProvider(minimumLength: Int, paddingString: String, fieldDataProvider: (instance: OUTPUT_TYPE?) -> Any) =
        PaddedFieldDataProvider(minimumLength, paddingString, fieldDataProvider)

    override fun <VALUE_TYPE> getCustomListFieldDataProvider(instances: Int, fieldDataProvider: (instance: OUTPUT_TYPE?) -> VALUE_TYPE) =
        CustomListFieldDataProvider(instances, fieldDataProvider)

    override fun <VALUE_TYPE> getCustomListRangeFieldDataProvider(
        minInstances: Int,
        maxInstances: Int,
        fieldDataProvider: (instance: OUTPUT_TYPE?) -> VALUE_TYPE
    ) = CustomListRangeFieldDataProvider(random, minInstances, maxInstances, fieldDataProvider)
}
