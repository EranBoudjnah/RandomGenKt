package com.mitteloupe.randomgenkt.fielddataprovider

private const val DEFAULT_DELIMITER = ""

/**
 * Created by Eran Boudjnah on 15/08/2018.
 */
class ConcatenateFieldDataProvider<OUTPUT_TYPE>
/**
 * Returns a new instance of [ConcatenateFieldDataProvider] generating a concatenated [String] of
 * the outputs generated by the provided lambda instances using the provided delimiter.
 *
 * @param delimiter          The delimiter to use when concatenating. Defaults to [DEFAULT_DELIMITER]
 * @param fieldDataProviders Concatenate the output of these FieldDataProvider instances
 */
@SafeVarargs
constructor(
	vararg fieldDataProviders: (OUTPUT_TYPE?) -> Any,
	private val delimiter: String = DEFAULT_DELIMITER
) : (OUTPUT_TYPE?) -> String {
	private val fieldDataProviders = fieldDataProviders.toList()

	override fun invoke(instance: OUTPUT_TYPE?): String {
		val result = StringBuilder()

		for (fieldDataProvider in fieldDataProviders) {
			result
				.append(fieldDataProvider.invoke(instance))
				.append(delimiter)
		}

		if (result.isNotEmpty() && delimiter.isNotEmpty()) {
			result.delete(result.length - delimiter.length, result.length)
		}

		return result.toString()
	}
}