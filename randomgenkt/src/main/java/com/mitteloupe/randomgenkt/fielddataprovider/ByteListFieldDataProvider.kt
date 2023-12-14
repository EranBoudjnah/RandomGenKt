package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

class ByteListFieldDataProvider<OUTPUT_TYPE>(
    private val random: Random,
    private val minSize: Int,
    private val maxSize: Int = minSize
) : FieldDataProvider<OUTPUT_TYPE, ByteArray>() {
    private val bytes by lazy { ByteArray(maxSize) }

    override fun invoke(instance: OUTPUT_TYPE?): ByteArray {
        random.nextBytes(bytes)
        val resultByteCount = random.nextInt(maxSize - minSize + 1) + minSize
        return bytes.take(resultByteCount).toByteArray()
    }
}
