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
            .append(word.first().toUpperCase())
            .append(word.substring(1).toLowerCase())
    }

    private fun addSpace() {
        stringBuilder.append(" ")
    }
}
