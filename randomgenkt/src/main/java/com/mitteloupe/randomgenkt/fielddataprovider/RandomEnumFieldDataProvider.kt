package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random
import kotlin.reflect.KClass

class RandomEnumFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE : Enum<*>>(
    private val random: Random,
    value: Class<VALUE_TYPE>
) : FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>() {
    constructor(
        random: Random,
        value: KClass<VALUE_TYPE>
    ) : this(random, value.java)

    private val possibleValues = value.enumConstants

    override fun invoke(instance: OUTPUT_TYPE?): VALUE_TYPE =
        possibleValues[random.nextInt(possibleValues.size)]
}
