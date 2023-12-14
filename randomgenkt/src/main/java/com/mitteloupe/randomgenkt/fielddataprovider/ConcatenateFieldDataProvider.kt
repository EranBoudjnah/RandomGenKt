package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider

private const val DEFAULT_DELIMITER = ""

class ConcatenateFieldDataProvider<INSTANCE>(
    vararg fieldDataProviders: (INSTANCE?) -> Any,
    private val delimiter: String = DEFAULT_DELIMITER
) : FieldDataProvider<INSTANCE, String>() {
    private val fieldDataProviders by lazy { fieldDataProviders.toList() }

    override fun invoke(instance: INSTANCE?): String {
        val result = StringBuilder()

        fieldDataProviders.forEach { fieldDataProvider ->
            result
                .append(fieldDataProvider(instance))
                .append(delimiter)
        }

        if (result.isNotEmpty() && delimiter.isNotEmpty()) {
            result.delete(result.length - delimiter.length, result.length)
        }

        return result.toString()
    }
}
