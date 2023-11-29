package com.mitteloupe.randomgenkt

import com.mitteloupe.randomgenkt.fielddataprovider.BooleanFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.ByteFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.ByteListFieldDataProvider
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
import com.mitteloupe.randomgenkt.fielddataprovider.ShortFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.UuidFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.WeightedFieldDataProvidersFieldDataProvider
import java.util.Random
import kotlin.reflect.KClass

internal open class SimpleFieldDataProviderFactory<OUTPUT_TYPE>(
    private val random: Random,
    private val uuidGenerator: UuidGenerator = DefaultUuidGenerator
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
        get() = LoremIpsumFieldDataProvider()

    override fun <VALUE_TYPE : Any> getExplicitFieldDataProvider(value: VALUE_TYPE) =
        ExplicitFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>(value)

    override fun <VALUE_TYPE : Any> getGenericListFieldDataProvider(
        fieldValueTypes: List<VALUE_TYPE>
    ) =
        GenericListFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>(random, fieldValueTypes)

    override fun getByteArrayFieldDataProvider(size: Int) =
        ByteListFieldDataProvider<OUTPUT_TYPE>(random, size)

    override fun getByteArrayFieldDataProvider(minimumSize: Int, maximumSize: Int) =
        ByteListFieldDataProvider<OUTPUT_TYPE>(random, minimumSize, maximumSize)

    override fun getDoubleFieldDataProvider(minimum: Double, maximum: Double) =
        DoubleFieldDataProvider<OUTPUT_TYPE>(random, minimum, maximum)

    override fun getFloatFieldDataProvider(minimum: Float, maximum: Float) =
        FloatFieldDataProvider<OUTPUT_TYPE>(random, minimum, maximum)

    override fun getIntFieldDataProvider(minimum: Int, maximum: Int) =
        IntFieldDataProvider<OUTPUT_TYPE>(random, minimum, maximum)

    override fun getLongFieldDataProvider(minimum: Long, maximum: Long) =
        LongFieldDataProvider<OUTPUT_TYPE>(random, minimum, maximum)

    override fun getShortFieldDataProvider(minimum: Short, maximum: Short) =
        ShortFieldDataProvider<OUTPUT_TYPE>(random, minimum, maximum)

    override fun getSequentialIntegerFieldDataProvider(startValue: Int) =
        SequentialIntegerFieldDataProvider<OUTPUT_TYPE>(startValue)

    override fun getRgbFieldDataProvider(provideAlpha: Boolean) =
        RgbFieldDataProvider<OUTPUT_TYPE>(random, provideAlpha)

    override fun getDateFieldDataProvider(earliestTimestamp: Long, latestTimestamp: Long) =
        DateFieldDataProvider<OUTPUT_TYPE>(random, earliestTimestamp, latestTimestamp)

    override fun getLoremIpsumFieldDataProvider(
        minimumLength: Int,
        maximumLength: Int,
        paragraphDelimiter: String?
    ) = if (paragraphDelimiter == null) {
        LoremIpsumFieldDataProvider(
            random,
            minimumLength = minimumLength,
            maximumLength = maximumLength
        )
    } else {
        LoremIpsumFieldDataProvider<OUTPUT_TYPE>(
            random,
            minimumLength = minimumLength,
            maximumLength = maximumLength,
            paragraphDelimiter = paragraphDelimiter
        )
    }

    override fun getWeightedFieldDataProvidersFieldDataProvider(
        fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, Any>
    ) = WeightedFieldDataProvidersFieldDataProvider(random, fieldDataProvider)

    override fun <ENUM_TYPE : Enum<*>> getRandomEnumFieldDataProvider(value: Class<ENUM_TYPE>) =
        RandomEnumFieldDataProvider<OUTPUT_TYPE, ENUM_TYPE>(random, value)

    override fun <ENUM_TYPE : Enum<*>> getRandomEnumFieldDataProvider(value: KClass<ENUM_TYPE>) =
        RandomEnumFieldDataProvider<OUTPUT_TYPE, ENUM_TYPE>(random, value)

    override fun getPaddedFieldDataProvider(
        fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, Any>,
        minimumLength: Int,
        paddingString: String
    ) = PaddedFieldDataProvider(minimumLength, paddingString, fieldDataProvider)

    override fun <VALUE_TYPE : Any> getCustomListFieldDataProvider(
        fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>,
        minimumInstances: Int,
        maximumInstances: Int
    ) = CustomListRangeFieldDataProvider(
        random,
        minimumInstances,
        maximumInstances,
        fieldDataProvider
    )
}
