package com.mitteloupe.randomgen.fielddataprovider

import com.mitteloupe.randomgen.FieldDataProvider

import java.util.Random

private const val DEFAULT_PARAGRAPH_DELIMITER = "\n\n"
private const val LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore" +
	"magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure" +
	"dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in" +
	"culpa qui officia deserunt mollit anim id est laborum."
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
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class LoremIpsumFieldDataProvider<OUTPUT_TYPE>
private constructor(
	private val minLength: Int,
	private val maxLength: Int,
	private val random: Random = FakeRandom(),
	private val paragraphDelimiter: String = DEFAULT_PARAGRAPH_DELIMITER
) : FieldDataProvider<OUTPUT_TYPE, String> {

	private constructor(pLength: Int = LOREM_IPSUM_LENGTH) : this(pLength, pLength)

	override fun generate(instance: OUTPUT_TYPE?): String {
		val remainingLength = random.nextInt(maxLength - minLength + 1) + minLength

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
			stringBuilder.isNotEmpty() -> stringBuilder.delete(stringBuilder.length - delimiterLength, stringBuilder.length)
		}

		return stringBuilder.toString()
	}

	private class FakeRandom : Random() {
		override fun nextInt(): Int {
			return 0
		}
	}

	companion object {
		/**
		 * Returns a new instance of [LoremIpsumFieldDataProvider] generating a `String` of Lorem Ipsum of the requested length.
		 *
		 * The Lorem Ipsum text is repeated if the requested length is longer than the length of Lorem Ipsum. In such cases, the default
		 * delimiter `DEFAULT_PARAGRAPH_DELIMITER` is used.
		 *
		 * @param pLength The length of the returned string. (Default: [LOREM_IPSUM_LENGTH])
		 */
		fun <OUTPUT_TYPE> getInstance(pLength: Int = LOREM_IPSUM_LENGTH): LoremIpsumFieldDataProvider<OUTPUT_TYPE> {
			return LoremIpsumFieldDataProvider(pLength)
		}

		/**
		 * Creates an instance of [LoremIpsumFieldDataProvider] generating a `String` of Lorem Ipsum with length in the range of
		 * `pMinLength` to `pMaxLength`.
		 *
		 * The Lorem Ipsum text is repeated if the generated length is longer than the length of Lorem Ipsum. In such cases, the default
		 * delimiter `DEFAULT_PARAGRAPH_DELIMITER` is used.
		 *
		 * @param pRandom    A random value generator
		 * @param pMinLength The minimum length of the returned string
		 * @param pMaxLength The maximum length of the returned string
		 * @param pParagraphDelimiter The delimiter to use for long Lorem Ipsums (Default: [DEFAULT_PARAGRAPH_DELIMITER])
		 */
		fun <OUTPUT_TYPE> getInstanceWithRange(
			pRandom: Random,
			pMinLength: Int,
			pMaxLength: Int,
			pParagraphDelimiter: String = DEFAULT_PARAGRAPH_DELIMITER
		): LoremIpsumFieldDataProvider<OUTPUT_TYPE> {
			return LoremIpsumFieldDataProvider(pMinLength, pMaxLength, pRandom, pParagraphDelimiter)
		}
	}
}