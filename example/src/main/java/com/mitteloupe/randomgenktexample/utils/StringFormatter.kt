package com.mitteloupe.randomgenktexample.utils

/**
 * Created by Eran Boudjnah on 29/08/2018.
 */
class StringFormatter<TYPE : Enum<*>> {
	private val mStringBuilder = StringBuilder()

	fun formatEnumValue(pEnum: TYPE): String {
		val words = getWordsFromEnum(pEnum)
		clearStringBuilder()
		concatWordsWithSpaces(words)
		return mStringBuilder.toString()
	}

	private fun getWordsFromEnum(pEnum: TYPE): Array<String> {
		return pEnum.toString().split("_".toRegex())
			.dropLastWhile { it.isEmpty() }
			.toTypedArray()
	}

	private fun clearStringBuilder() {
		mStringBuilder.delete(0, mStringBuilder.length)
	}

	private fun concatWordsWithSpaces(pWords: Array<String>) {
		val lastCharacter = pWords.size - 1

		pWords.forEachIndexed { characterPosition, word ->
			addCapitalizedWord(word)
			if (characterPosition < lastCharacter) {
				mStringBuilder.append(" ")
			}
		}
	}

	private fun addCapitalizedWord(pWord: String) {
		mStringBuilder
			.append(pWord.first().toUpperCase())
			.append(pWord.substring(1).toLowerCase())
	}
}
