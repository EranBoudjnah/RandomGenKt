package com.mitteloupe.randomgenktexample.utils

/**
 * Created by Eran Boudjnah on 29/08/2018.
 */
object StringFormatter {
	private val stringBuilder = StringBuilder()

	fun <TYPE : Enum<*>> formatEnumValue(enum: TYPE): String {
		val words = getWordsFromEnum(enum)
		clearStringBuilder()
		concatWordsWithSpaces(words)
		return stringBuilder.toString()
	}

	private fun <TYPE : Enum<*>> getWordsFromEnum(enum: TYPE): Array<String> {
		return enum.toString().split("_".toRegex())
			.dropLastWhile { it.isEmpty() }
			.toTypedArray()
	}

	private fun clearStringBuilder() {
		stringBuilder.delete(0, stringBuilder.length)
	}

	private fun concatWordsWithSpaces(pWords: Array<String>) {
		val lastCharacter = pWords.size - 1

		pWords.forEachIndexed { characterPosition, word ->
			addCapitalizedWord(word)
			if (characterPosition < lastCharacter) {
				stringBuilder.append(" ")
			}
		}
	}

	private fun addCapitalizedWord(pWord: String) {
		stringBuilder
			.append(pWord.first().toUpperCase())
			.append(pWord.substring(1).toLowerCase())
	}
}
