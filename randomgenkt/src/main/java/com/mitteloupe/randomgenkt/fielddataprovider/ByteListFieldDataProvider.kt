package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider

import java.util.ArrayList
import java.util.Random

/**
 * A [FieldDataProvider] that generates a [Byte] list.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class ByteListFieldDataProvider<OUTPUT_TYPE>
private constructor(
    private val random: Random,
    private val minSize: Int,
    private val maxSize: Int
) : FieldDataProvider<OUTPUT_TYPE, List<Byte>>() {
    private constructor(
        random: Random,
        size: Int
    ) : this(random, size, size)

    private val bytes by lazy { ByteArray(maxSize) }

    override fun invoke(instance: OUTPUT_TYPE?): List<Byte> {
        random.nextBytes(bytes)
        val items = random.nextInt(maxSize - minSize + 1) + minSize
        val bytes = ArrayList<Byte>(items)
        repeat(items) { index ->
            bytes.add(this.bytes[index])
        }
        return bytes
    }

    companion object {
        /**
		 * Returns a new instance of [ByteListFieldDataProvider] generating precisely [size] [Byte]s.
		 *
		 * @param random A random value generator
		 * @param size The number of [Byte]s to generate
		 */
        fun <OUTPUT_TYPE> getInstanceWithSize(random: Random, size: Int) =
            ByteListFieldDataProvider<OUTPUT_TYPE>(random, size)

        /**
		 * Returns a new instance of [ByteListFieldDataProvider] generating between [minSize] and [maxSize] [Byte]s (inclusive).
		 *
		 * @param random A random value generator
		 * @param minSize The minimal number of [Byte]s to generate
		 * @param maxSize The maximal number of [Byte]s to generate
		 */
        fun <OUTPUT_TYPE> getInstanceWithSizeRange(random: Random, minSize: Int, maxSize: Int) =
            ByteListFieldDataProvider<OUTPUT_TYPE>(random, minSize, maxSize)
    }
}