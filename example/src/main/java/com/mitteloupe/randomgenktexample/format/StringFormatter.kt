package com.mitteloupe.randomgenktexample.format

import java.util.*

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

    private fun concatWordsWithSpaces(words: Array<String>) {
        val lastCharacter = words.size - 1

        words.forEachIndexed { characterPosition, word ->
            addCapitalizedWord(word)
            if (characterPosition < lastCharacter) {
                addSpace()
            }
        }
    }

    private fun addCapitalizedWord(word: String) {
        stringBuilder
            .append(word.first().uppercaseChar())
            .append(word.substring(1).lowercase(Locale.ENGLISH))
    }

    private fun addSpace() {
        stringBuilder.append(" ")
    }
}
