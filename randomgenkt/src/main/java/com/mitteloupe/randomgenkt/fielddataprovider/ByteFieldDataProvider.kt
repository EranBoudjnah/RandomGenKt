package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

class ByteFieldDataProvider<OUTPUT_TYPE>(
    private val random: Random
) : FieldDataProvider<OUTPUT_TYPE, Byte>() {
    private val byteArray by lazy { ByteArray(1) }

    override fun invoke(instance: OUTPUT_TYPE?): Byte {
        random.nextBytes(byteArray)
        return byteArray[0]
    }
}
