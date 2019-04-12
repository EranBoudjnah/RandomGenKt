package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider

import java.math.BigDecimal
import java.util.Date
import java.util.Random

/**
 * A [FieldDataProvider] that generates a [Date] value.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class DateFieldDataProvider<OUTPUT_TYPE>
/**
 * Returns a new instance of [DateFieldDataProvider] generating a [Date] between [earliestTimestamp] and [latestTimestamp].
 *
 * @param random A random value generator
 * @param earliestTimestamp Milliseconds since January 1, 1970, 00:00:00 GMT.
 * @param latestTimestamp Milliseconds since January 1, 1970, 00:00:00 GMT.
 */
constructor(
    private val random: Random,
    private val earliestTimestamp: Long = 0L,
    private val latestTimestamp: Long = Long.MAX_VALUE
) : FieldDataProvider<OUTPUT_TYPE, Date>() {
    override fun invoke(instance: OUTPUT_TYPE?): Date {
        val min = BigDecimal.valueOf(earliestTimestamp)
        val max = BigDecimal.valueOf(latestTimestamp)
        val value = max
            .subtract(min)
            .add(BigDecimal.valueOf(1))
            .multiply(BigDecimal.valueOf(random.nextDouble()))
            .add(min)

        return Date(value.toLong())
    }
}
