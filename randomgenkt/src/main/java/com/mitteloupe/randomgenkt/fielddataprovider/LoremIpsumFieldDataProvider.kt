package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

private const val DEFAULT_PARAGRAPH_DELIMITER = "\n\n"
private const val LOREM_IPSUM =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
        "ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
        "ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in " +
        "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
        "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt " +
        "mollit anim id est laborum."
private const val LOREM_IPSUM_LENGTH = LOREM_IPSUM.length

/**
 * Provides strings of Lorem Ipsum.
 *
 * See: [https://www.lipsum.com/](https://www.lipsum.com/)
 *
 * TODO
 * Introduce Options:
 * Shuffle sentences
 * Hard cut, Trim at word, Trim at sentence, Ellipsize
 * Use builder pattern
 */
class LoremIpsumFieldDataProvider<OUTPUT_TYPE>(
    private val random: Random = FakeRandom(),
    private val minimumLength: Int = LOREM_IPSUM_LENGTH,
    private val maximumLength: Int = minimumLength,
    private val paragraphDelimiter: String = DEFAULT_PARAGRAPH_DELIMITER
) : FieldDataProvider<OUTPUT_TYPE, String>() {
    override fun invoke(instance: OUTPUT_TYPE?): String {
        val remainingLength = random.nextInt(maximumLength - minimumLength + 1) + minimumLength

        return getLoremIpsumString(remainingLength)
    }

    private fun getLoremIpsumString(stringLength: Int): String {
        val delimiterLength = paragraphDelimiter.length
        val stringBuilder = StringBuilder()
        var remainingLength = stringLength

        while (remainingLength >= LOREM_IPSUM_LENGTH + delimiterLength) {
            stringBuilder
                .append(LOREM_IPSUM)
                .append(paragraphDelimiter)
            remainingLength -= LOREM_IPSUM_LENGTH + delimiterLength
        }

        when {
            remainingLength != 0 -> stringBuilder.append(LOREM_IPSUM.substring(0, remainingLength))
            stringBuilder.isNotEmpty() -> stringBuilder.delete(
                stringBuilder.length - delimiterLength,
                stringBuilder.length
            )
        }

        return stringBuilder.toString()
    }

    private class FakeRandom : Random() {
        override fun nextInt() = 0
    }
}
