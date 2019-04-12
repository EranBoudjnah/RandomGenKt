package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

/**
 * A [FieldDataProvider] that generates an `Enum` value.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class RandomEnumFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE : Enum<*>>
/**
 * Creates an instance of [RandomEnumFieldDataProvider] generating a random `Enum` value.
 *
 * @param random A random value generator
 * @param value An enum class of which values will be generated
 */
constructor(
    private val random: Random,
    value: Class<VALUE_TYPE>
) : FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>() {
    private val possibleValues = value.enumConstants

    override fun invoke(instance: OUTPUT_TYPE?): VALUE_TYPE = possibleValues[random.nextInt(possibleValues.size)]
}
