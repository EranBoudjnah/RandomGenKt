package com.mitteloupe.randomgenkt

import com.mitteloupe.randomgenkt.fielddataprovider.RgbFieldDataProvider
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.reflect.Modifier
import java.util.Random

/**
 * Created by Eran Boudjnah on 11/08/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class SimpleFieldDataProviderFactoryTest {
	private lateinit var cut: SimpleFieldDataProviderFactory<Any>

	@Mock
	private lateinit var random: Random
	@Mock
	private lateinit var uuidGenerator: UuidGenerator

	@Before
	fun setUp() {
		cut = SimpleFieldDataProviderFactory(random, uuidGenerator)
	}

	@Test
	fun `When getExplicitFieldDataProvider then returns instance with expected value`() {
		// Given
		val popcorn = "Popcorn"

		// When
		val dataProvider = cut.getExplicitFieldDataProvider(popcorn)

		// Then
		assertFieldEquals(dataProvider, "value", popcorn)
	}

	@Test
	fun `Given list of values when getGenericListFieldDataProvider then returns instance with correct field values`() {
		// Given
		val list = listOf("A", "B")

		// When
		val dataProvider = cut.getGenericListFieldDataProvider(list)

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "list", list)
	}

	@Test
	fun `When getBooleanFieldDataProvider then returns instance with random set`() {
		// When
		val dataProvider = cut.booleanFieldDataProvider

		// Then
		assertFieldEquals(dataProvider, "random", random)
	}

	@Test
	fun `When getByteFieldDataProvider then returns instance with RandomSet`() {
		// When
		val dataProvider = cut.byteFieldDataProvider

		// Then
		assertFieldEquals(dataProvider, "random", random)
	}

	@Test
	fun `Given fixed size when getByteListFieldDataProvider then returns instance with correct field values`() {
		// Given
		val size = 4

		// When
		val dataProvider = cut.getByteListFieldDataProvider(size)

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "minSize", size)
		assertFieldEquals(dataProvider, "maxSize", size)
	}

	@Test
	fun `Given ranged size when getByteListFieldDataProvider then returns instance with correct field values`() {
		// Given
		val minSize = 3
		val maxSize = 5

		// When
		val dataProvider = cut.getByteListFieldDataProvider(minSize, maxSize)

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "minSize", minSize)
		assertFieldEquals(dataProvider, "maxSize", maxSize)
	}

	@Test
	fun `When getDoubleFieldDataProvider then returns instance with correct min and max`() {
		// When
		val dataProvider = cut.getDoubleFieldDataProvider()

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "minimum", 0.0)
		assertFieldEquals(dataProvider, "maximum", 1.0)
	}

	@Test
	fun `Given range when getDoubleFieldDataProvider then returns instance with correct min and max`() {
		// Given
		val minValue = -3.0
		val maxValue = 3.0

		// When
		val dataProvider = cut.getDoubleFieldDataProvider(minValue, maxValue)

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "minimum", minValue)
		assertFieldEquals(dataProvider, "maximum", maxValue)
	}

	@Test
	fun `When getFloatFieldDataProvider then returns instance with correct min and max`() {
		// When
		val dataProvider = cut.getFloatFieldDataProvider()

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "minimum", 0f)
		assertFieldEquals(dataProvider, "maximum", 1f)
	}

	@Test
	fun `Given range when getFloatFieldDataProvider then returns instance with correct min and max`() {
		// Given
		val minValue = -3f
		val maxValue = 3f

		// When
		val dataProvider = cut.getFloatFieldDataProvider(minValue, maxValue)

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "minimum", minValue)
		assertFieldEquals(dataProvider, "maximum", maxValue)
	}

	@Test
	fun `When getIntFieldDataProvider then returns instance with correct min and max`() {
		// When
		val dataProvider = cut.getIntFieldDataProvider()

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "minimum", Integer.MIN_VALUE)
		assertFieldEquals(dataProvider, "maximum", Integer.MAX_VALUE)
	}

	@Test
	fun `Given range when getIntFieldDataProvider then returns instance with correct min and max`() {
		// Given
		val minValue = -3
		val maxValue = 3

		// When
		val dataProvider = cut.getIntFieldDataProvider(minValue, maxValue)

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "minimum", minValue)
		assertFieldEquals(dataProvider, "maximum", maxValue)
	}

	@Test
	fun `When getLongFieldDataProvider then returns instance with correct min and max`() {
		// When
		val dataProvider = cut.getLongFieldDataProvider()

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "minimum", java.lang.Long.MIN_VALUE)
		assertFieldEquals(dataProvider, "maximum", java.lang.Long.MAX_VALUE)
	}

	@Test
	fun `Given range when getLongFieldDataProvider then returns instance with correct min and max`() {
		// Given
		val minValue = -3L
		val maxValue = 3L

		// When
		val dataProvider = cut.getLongFieldDataProvider(minValue, maxValue)

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "minimum", minValue)
		assertFieldEquals(dataProvider, "maximum", maxValue)
	}

	@Test
	fun `When getSequentialIntegerFieldDataProvider then returns instance with counter at default`() {
		// When
		val dataProvider = cut.sequentialIntegerFieldDataProvider

		// Then
		assertFieldEquals(dataProvider, "counter", 1)
	}

	@Test
	fun `Given initial value when getSequentialIntegerFieldDataProvider then returns instance with counter set`() {
		// When
		val startValue = 42
		val dataProvider = cut.getSequentialIntegerFieldDataProvider(startValue)

		// Then
		assertFieldEquals(dataProvider, "counter", startValue)
	}

	@Test
	fun `When getUuidFieldDataProvider then returns instance with UuidGenerator set`() {
		// When
		val dataProvider = cut.uuidFieldDataProvider

		// Then
		assertFieldEquals(dataProvider, "uuidGenerator", uuidGenerator)
	}

	@Test
	fun `Given alpha when getRgbFieldDataProvider then returns instance with correct alpha flag and random set`() {
		// When
		var dataProvider: RgbFieldDataProvider<*> = cut.getRgbFieldDataProvider(true)

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "provideAlpha", true)

		// When
		dataProvider = cut.getRgbFieldDataProvider(false)

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "provideAlpha", false)
	}

	@Test
	fun `When getLoremIpsumFieldDataProvider then returns instance with min and maxLength set to one whole Lorem Ipsum`() {
		// When
		val dataProvider = cut.loremIpsumFieldDataProvider

		// Then
		assertFieldEquals(dataProvider, "minLength", 442)
		assertFieldEquals(dataProvider, "maxLength", 442)
	}

	@Test
	fun `Given length when getLoremIpsumFieldDataProvider then returns instance with correct properties set`() {
		// Given
		val length = 64

		// When
		val dataProvider = cut.getLoremIpsumFieldDataProvider(length)

		// Then
		assertFieldEquals(dataProvider, "minLength", length)
		assertFieldEquals(dataProvider, "maxLength", length)
		assertFieldEquals(dataProvider, "paragraphDelimiter", "\n\n")
	}

	@Test
	fun `Given ranged length when getLoremIpsumFieldDataProvider then returns instance with correct properties set`() {
		// Given
		val minLength = 74
		val maxLength = 75

		// When
		val dataProvider = cut.getLoremIpsumFieldDataProvider(minLength, maxLength)

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "minLength", minLength)
		assertFieldEquals(dataProvider, "maxLength", maxLength)
		assertFieldEquals(dataProvider, "paragraphDelimiter", "\n\n")
	}

	@Test
	fun `Given range, length and delimiter when getLoremIpsumFieldDataProvider then returns instance with correct properties set`() {
		// Given
		val minLength = 74
		val maxLength = 75
		val paragraphDelimiter = "... "

		// When
		val dataProvider = cut.getLoremIpsumFieldDataProvider(minLength, maxLength, paragraphDelimiter)

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "minLength", minLength)
		assertFieldEquals(dataProvider, "maxLength", maxLength)
		assertFieldEquals(dataProvider, "paragraphDelimiter", paragraphDelimiter)
	}

	@Test
	fun `When getRandomEnumFieldDataProvider then returns instance with random set`() {
		// When
		val dataProvider = cut.getRandomEnumFieldDataProvider(Rings::class.java)

		// Then
		assertFieldEquals(dataProvider, "random", random)
		val values = getFieldValue<Array<Rings>>(dataProvider, "possibleValues")
		assertEquals(listOf(Rings.ONE_RING_TO_RULE_THEM_ALL, Rings.ONE_RING_TO_FIND_THEM, Rings.ONE_RING_TO_BRING_THEM_ALL),
			values.asList())
	}

	@Test
	fun `Given params and fieldDataProvider when getPaddedFieldDataProvider then returns instance with correct properties set`() {
		// Given
		val paddingString = "00"
		val minimumLength = 2
		val provider = mock<(Any?) -> String>()

		// When
		val dataProvider = cut.getPaddedFieldDataProvider(minimumLength, paddingString, provider)

		// Then
		assertFieldEquals(dataProvider, "minimumLength", minimumLength)
		assertFieldEquals(dataProvider, "paddingString", paddingString)
		assertFieldEquals(dataProvider, "fieldDataProvider", provider)
	}

	@Test
	fun `Given instances count and fieldDataProvider when getCustomListFieldDataProvider then returns instance with correct properties set`() {
		// Given
		val instances = 5
		val provider = mock<(Any?) -> String>()

		// When
		val dataProvider = cut.getCustomListFieldDataProvider(instances, provider)

		// Then
		assertFieldEquals(dataProvider, "instances", instances)
		assertFieldEquals(dataProvider, "fieldDataProvider", provider)
	}

	@Test
	fun `Given instances count, range and fieldDataProvider when getCustomListRangeFieldDataProvider then returns instance with correct properties set`() {
		// Given
		val provider = mock<(Any?) -> String>()
		val minInstances = 5
		val maxInstances = 5

		// When
		val dataProvider = cut.getCustomListRangeFieldDataProvider(minInstances, maxInstances, provider)

		// Then
		assertFieldEquals(dataProvider, "random", random)
		assertFieldEquals(dataProvider, "minInstances", minInstances)
		assertFieldEquals(dataProvider, "maxInstances", maxInstances)
		assertFieldEquals(dataProvider, "fieldDataProvider", provider)
	}


	private fun <DATA_TYPE> assertFieldEquals(pObject: Any, pFieldName: String, pExpectedValue: DATA_TYPE?) {
		val value = getFieldValue<DATA_TYPE>(pObject, pFieldName)

		assertEquals(pExpectedValue, value)
	}

	private fun <DATA_TYPE> getFieldValue(fieldOwner: Any, fieldName: String): DATA_TYPE {
		val field = fieldOwner.javaClass.getDeclaredField(fieldName)

		if (Modifier.isPrivate(field.modifiers)) {
			field.isAccessible = true
		}

		@Suppress("UNCHECKED_CAST")
		return field.get(fieldOwner) as DATA_TYPE
	}

	private enum class Rings {
		ONE_RING_TO_RULE_THEM_ALL,
		ONE_RING_TO_FIND_THEM,
		ONE_RING_TO_BRING_THEM_ALL
	}
}