package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.math.BigDecimal
import java.util.Date
import java.util.Random

class DateFieldDataProvider<OUTPUT_TYPE>(
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
