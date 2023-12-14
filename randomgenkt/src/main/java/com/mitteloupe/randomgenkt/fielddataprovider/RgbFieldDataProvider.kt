package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

class RgbFieldDataProvider<OUTPUT_TYPE>(
    private val random: Random,
    private val provideAlpha: Boolean
) : FieldDataProvider<OUTPUT_TYPE, String>() {
    override fun invoke(instance: OUTPUT_TYPE?): String {
        val red = random.nextInt(255)
        val green = random.nextInt(255)
        val blue = random.nextInt(255)
        return when {
            provideAlpha -> {
                val alpha = random.nextInt(255)
                String.format("#%02x%02x%02x%02x", alpha, red, green, blue)
            }

            else -> String.format("#%02x%02x%02x", red, green, blue)
        }
    }
}
