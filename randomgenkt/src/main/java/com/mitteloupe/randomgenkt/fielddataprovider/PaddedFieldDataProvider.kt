package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider

/**
 * A [FieldDataProvider] that pads generated data (as `String`) with repetitions of the provided `String`.
 *
 * Created by Eran Boudjnah on 16/08/2018.
 */
class PaddedFieldDataProvider<OUTPUT_TYPE>
/**
 * Creates an instance of [PaddedFieldDataProvider] generating a padded `String` of
 * the output generated by the provided [FieldDataProvider] instance using an infinitely repeated `String` of copies of
 * the provided padding `String`.
 *
 * If the `String` generated by the [FieldDataProvider] instance is not shorter than the provided minimum length, it is returned
 * as is.
 *
 * @param minimumLength     The minimal returned String length
 * @param paddingString     The string to use for padding
 * @param fieldDataProvider A provider for strings to pad
 */
constructor(
	private val minimumLength: Int,
	private val paddingString: String,
	private val fieldDataProvider: (OUTPUT_TYPE?) -> Any
) : (OUTPUT_TYPE?) -> String {
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

		if (stringBuilder.length > paddingLength && paddingLength > 0) {
			stringBuilder.delete(paddingLength, stringBuilder.length)
		}

		return stringBuilder
	}

	private fun getGeneratedString(instance: OUTPUT_TYPE?) = fieldDataProvider.invoke(instance).toString()
}