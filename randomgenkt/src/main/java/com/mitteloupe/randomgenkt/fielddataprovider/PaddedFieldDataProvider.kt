package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider

class PaddedFieldDataProvider<OUTPUT_TYPE>(
    private val minimumLength: Int,
    private val paddingString: String,
    private val fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, Any>
) : FieldDataProvider<OUTPUT_TYPE, String>() {
    override fun invoke(instance: OUTPUT_TYPE?): String {
        val generatedString = getGeneratedString(instance)

        val charactersMissing = minimumLength - generatedString.length

        val stringBuilder = getStringBuilderWithPadding(charactersMissing)

        return stringBuilder
            .append(generatedString)
            .toString()
    }

    private fun getStringBuilderWithPadding(paddingLength: Int): StringBuilder {
        val stringBuilder = StringBuilder()

        while (paddingString.isNotEmpty() && stringBuilder.length < paddingLength) {
            stringBuilder.append(paddingString)
        }

        if (isPaddingProvided(paddingLength) && isStringLongerThanPadding(
                stringBuilder,
                paddingLength
            )
        ) {
            stringBuilder.delete(paddingLength, stringBuilder.length)
        }

        return stringBuilder
    }

    private fun isStringLongerThanPadding(stringBuilder: StringBuilder, paddingLength: Int) =
        stringBuilder.length > paddingLength

    private fun isPaddingProvided(paddingLength: Int) = paddingLength > 0

    private fun getGeneratedString(instance: OUTPUT_TYPE?) = fieldDataProvider(instance).toString()
}
