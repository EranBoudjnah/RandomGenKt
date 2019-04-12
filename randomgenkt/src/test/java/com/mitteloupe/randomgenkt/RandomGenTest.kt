package com.mitteloupe.randomgenkt

import com.mitteloupe.randomgenkt.fielddataprovider.BooleanFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.ByteFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.ByteListFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.CustomListFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.CustomListRangeFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.DoubleFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.ExplicitFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.FloatFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.GenericListFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.IntFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.LongFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.LoremIpsumFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.RandomEnumFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.RgbFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.SequentialIntegerFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.UuidFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.WeightedFieldDataProvidersFieldDataProvider
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import java.util.Arrays

/**
 * Created by Eran Boudjnah on 07/08/2018.
 */
@RunWith(Parameterized::class)
class RandomGenTest(
    private val incompleteBuilderField: RandomGen.IncompleteBuilderField<TestPerson>,
    private val fieldDataProviderFactory: FieldDataProviderFactory<TestPerson>
) {

    companion object {
        private const val EQUALS_PRECISION = 0.0001

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<*>> {
            val factory = mock<FieldDataProviderFactory<TestPerson>>()
            val incompleteBuilderFieldWithProviderAndFactory = RandomGen.Builder<TestPerson>()
                .withFactoryAndProvider(factory) { TestPerson() }

            val incompleteBuilderFieldOfClassAndFactory = RandomGen.Builder<TestPerson>()
                .ofClassWithFactory<TestPerson>(factory)

            return Arrays.asList(
                arrayOf(incompleteBuilderFieldWithProviderAndFactory, factory),
                arrayOf(incompleteBuilderFieldOfClassAndFactory, factory)
            )
        }
    }

    private lateinit var cut: RandomGen<TestPerson>

    @Before
    fun setUp() {
        reset(fieldDataProviderFactory)
    }

    @Test
    fun `Given builder returning explicitly when generate then instance has correct value`() {
        // Given
        val name = "Superman"

        val explicitFieldDataProvider = mock<ExplicitFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name)).willReturn(explicitFieldDataProvider)
        given(explicitFieldDataProvider.invoke(any<TestPerson>())).willReturn(name)

        cut = incompleteBuilderField
            .withField("name")
            .returningExplicitly(name)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(name, testPerson.name)
    }

    @Test
    fun `Given builder returning explicitly for lazy field when generate then instance has correct value`() {
        // Given
        val explicitFieldDataProvider = mock<ExplicitFieldDataProvider<TestPerson, Boolean>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(true)).willReturn(explicitFieldDataProvider)
        given(explicitFieldDataProvider.invoke(any<TestPerson>())).willReturn(true)

        cut = incompleteBuilderField
            .withField("isLazy")
            .returningExplicitly(true)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertTrue(testPerson.isLazy)
    }

    @Test
    fun `Given builder returning from list when generate then instance has correct value`() {
        // Given
        val name1 = "Rocksteady"
        val name2 = "Bebop"

        val namesList = Arrays.asList(name1, name2)
        val genericListFieldDataProvider = mock<GenericListFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getGenericListFieldDataProvider(namesList)).willReturn(genericListFieldDataProvider)
        given(genericListFieldDataProvider.invoke(any<TestPerson>())).willReturn(name2)

        cut = incompleteBuilderField
            .withField("name")
            .returning(namesList)
            .build()

        // When
        var testPerson = cut.generate()

        // Then
        assertEquals(name2, testPerson.name)

        // Given
        given(genericListFieldDataProvider.invoke(any<TestPerson>())).willReturn(name1)

        // When
        testPerson = cut.generate()

        // Then
        assertEquals(name1, testPerson.name)
    }

    @Test
    fun `Given builder returning Boolean when generate then instance has correct value`() {
        // Given
        val booleanFieldDataProvider = mock<BooleanFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.booleanFieldDataProvider).willReturn(booleanFieldDataProvider)
        given(booleanFieldDataProvider.invoke(any<TestPerson>())).willReturn(false)

        cut = incompleteBuilderField
            .withField("isBrave")
            .returningBoolean()
            .build()

        // When
        var testPerson = cut.generate()

        // Then
        assertFalse(testPerson.isBrave)

        // Given
        given(booleanFieldDataProvider.invoke(any<TestPerson>())).willReturn(true)

        // When
        testPerson = cut.generate()

        // Then
        assertTrue(testPerson.isBrave)
    }

    @Test
    fun `Given builder returning Byte when generate then instance has correct value`() {
        // Given
        val byteFieldDataProvider = mock<ByteFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.byteFieldDataProvider).willReturn(byteFieldDataProvider)
        given(byteFieldDataProvider.invoke(any<TestPerson>())).willReturn(3.toByte())

        cut = incompleteBuilderField
            .withField("bite")
            .returningByte()
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(3.toByte().toLong(), testPerson.bite.toLong())
    }

    @Test
    fun `Given builder returning Byte list when generate then instance has correct value`() {
        // Given
        val byteListFieldDataProvider = mock<ByteListFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getByteListFieldDataProvider(5)).willReturn(byteListFieldDataProvider)
        val byteList = listOf(1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte())
        given(byteListFieldDataProvider.invoke(any<TestPerson>())).willReturn(byteList)

        cut = incompleteBuilderField
            .withField("bites")
            .returningBytes(5)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(byteList, Arrays.asList(*testPerson.bites!!))
    }

    @Test
    fun `Given builder returning Byte list with range when generate then instance has correct value`() {
        // Given
        val byteListFieldDataProvider = mock<ByteListFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getByteListFieldDataProvider(4, 5)).willReturn(byteListFieldDataProvider)
        val byteList = Arrays.asList(1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte())
        given(byteListFieldDataProvider.invoke(any<TestPerson>())).willReturn(byteList)

        cut = incompleteBuilderField
            .withField("bites")
            .returningBytes(4, 5)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(byteList, Arrays.asList(*testPerson.bites!!))
    }

    @Test
    fun `Given builder returning Double when generate then instance has correct value`() {
        // Given
        val doubleFieldDataProvider = mock<DoubleFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getDoubleFieldDataProvider()).willReturn(doubleFieldDataProvider)
        val expectedValue = 1.2345
        given(doubleFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("wealth")
            .returningDouble()
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue, testPerson.wealth, 0.0001)
    }

    @Test
    fun `Given builder returning Double range when generate then instance has correct value`() {
        // Given
        val doubleRangeFieldDataProvider = mock<DoubleFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getDoubleFieldDataProvider(1.0, 2.0)).willReturn(doubleRangeFieldDataProvider)
        val expectedValue = 1.2345
        given(doubleRangeFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("wealth")
            .returning(1.0, 2.0)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue, testPerson.wealth, 0.0001)
    }

    @Test
    fun `Given builder returning Float when generate then instance has correct value`() {
        // Given
        val floatFieldDataProvider = mock<FloatFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getFloatFieldDataProvider()).willReturn(floatFieldDataProvider)
        val expectedValue = 1.23f
        given(floatFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("height")
            .returningFloat()
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue.toDouble(), testPerson.height.toDouble(), 0.0001)
    }

    @Test
    fun `Given builder returning Float range when generate then instance has correct value`() {
        // Given
        val floatRangeFieldDataProvider = mock<FloatFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getFloatFieldDataProvider(1f, 2f)).willReturn(floatRangeFieldDataProvider)
        val expectedValue = 1.23f
        given(floatRangeFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("height")
            .returning(1f, 2f)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue.toDouble(), testPerson.height.toDouble(), EQUALS_PRECISION)
    }

    @Test
    fun `Given builder returning Integer when generate then instance has correct value`() {
        // Given
        val integerFieldDataProvider = mock<IntFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getIntFieldDataProvider()).willReturn(integerFieldDataProvider)
        val expectedValue = 400
        given(integerFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("candiesCount")
            .returningInt()
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue.toLong(), testPerson.candiesCount.toLong())
    }

    @Test
    fun `Given builder returning Integer range when generate then instance has correct value`() {
        // Given
        val integerFieldDataProvider = mock<IntFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getIntFieldDataProvider(300, 500)).willReturn(integerFieldDataProvider)
        val expectedValue = 400
        given(integerFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("candiesCount")
            .returning(300, 500)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue.toLong(), testPerson.candiesCount.toLong())
    }

    @Test
    fun `Given builder returning Long when generate then instance has correct value`() {
        // Given
        val longFieldDataProvider = mock<LongFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getLongFieldDataProvider()).willReturn(longFieldDataProvider)
        val expectedValue = 1337L
        given(longFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("soLong")
            .returningLong()
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue, testPerson.soLong)
    }

    @Test
    fun `Given builder returning Long range when generate then instance has correct value`() {
        // Given
        val longFieldDataProvider = mock<LongFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getLongFieldDataProvider(1000L, 2000L)).willReturn(longFieldDataProvider)
        val expectedValue = 1337L
        given(longFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("soLong")
            .returning(1000L, 2000L)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue, testPerson.soLong)
    }

    @Test
    fun `Given builder returning sequential Integer when generate then instance has correct value`() {
        // Given
        val sequentialIntegerFieldDataProvider = mock<SequentialIntegerFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.sequentialIntegerFieldDataProvider).willReturn(sequentialIntegerFieldDataProvider)
        val expectedValue = 1234567
        given(sequentialIntegerFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("id")
            .returningSequentialInteger()
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue.toLong(), testPerson.id.toLong())
    }

    @Test
    fun `Given builder returning sequential Integer with start value when generate then instance has correct value`() {
        // Given
        val sequentialIntegerFieldDataProvider = mock<SequentialIntegerFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getSequentialIntegerFieldDataProvider(5)).willReturn(sequentialIntegerFieldDataProvider)
        val expectedValue = 5
        given(sequentialIntegerFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("id")
            .returningSequentialInteger(5)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue.toLong(), testPerson.id.toLong())
    }

    @Test
    fun `Given builder returning UUID when generate then instance has correct value`() {
        // Given
        val uuidFieldDataProvider = mock<UuidFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.uuidFieldDataProvider).willReturn(uuidFieldDataProvider as (TestPerson?) -> String)
        val expectedValue = "8b3728d0-9c1d-11e8-98d0-529269fb1459"
        given(uuidFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("uuid")
            .returningUuid()
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue, testPerson.uuid)
    }

    @Test
    fun `Given builder returning RGB when generate then instance has correct value`() {
        // Given
        val rgbFieldDataProvider = mock<RgbFieldDataProvider<TestPerson>>()
        val rgbaFieldDataProvider = mock<RgbFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getRgbFieldDataProvider(false)).willReturn(rgbFieldDataProvider as (TestPerson?) -> String)
        given(fieldDataProviderFactory.getRgbFieldDataProvider(true)).willReturn(rgbaFieldDataProvider as (TestPerson?) -> String)
        val expectedValueRGB = "#AABBAA"
        val expectedValueRGBA = "#FFAABBAA"
        given(rgbFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValueRGB)
        given(rgbaFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValueRGBA)

        cut = incompleteBuilderField
            .withField("shirtColor")
            .returningRgb(true)
            .build()

        // When
        var testPerson = cut.generate()

        // Then
        assertEquals(expectedValueRGBA, testPerson.shirtColor)

        // Given
        cut = incompleteBuilderField
            .withField("shirtColor")
            .returningRgb(false)
            .build()

        // When
        testPerson = cut.generate()

        // Then
        assertEquals(expectedValueRGB, testPerson.shirtColor)
    }

    @Test
    fun `Given builder returning Lorem Ipsum when generate then instance has correct value`() {
        // Given
        val loremIpsumFieldDataProvider = mock<LoremIpsumFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.loremIpsumFieldDataProvider).willReturn(loremIpsumFieldDataProvider as (TestPerson?) -> String)
        val expectedValue = "Lorem ipsum and stuff"
        given(loremIpsumFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("biography")
            .returningLoremIpsum()
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue, testPerson.biography)
    }

    @Test
    fun `Given builder returning Lorem Ipsum with length when generate then instance has correct value`() {
        // Given
        val loremIpsumFieldDataProvider = mock<LoremIpsumFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getLoremIpsumFieldDataProvider(21)).willReturn(loremIpsumFieldDataProvider as (TestPerson?) -> String)
        val expectedValue = "Lorem ipsum and stuff"
        given(loremIpsumFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("biography")
            .returningLoremIpsum(21)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue, testPerson.biography)
    }

    @Test
    fun `Given builder returning Lorem Ipsum with length range when generate then instance has correct value`() {
        // Given
        val loremIpsumFieldDataProvider = mock<LoremIpsumFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getLoremIpsumFieldDataProvider(20, 22)).willReturn(loremIpsumFieldDataProvider as (TestPerson?) -> String)
        val expectedValue = "Lorem ipsum and stuff"
        given(loremIpsumFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("biography")
            .returningLoremIpsum(20, 22)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue, testPerson.biography)
    }

    @Test
    fun `Given builder returning Lorem Ipsum with length range and delimiter when generate then instance has correct value`() {
        // Given
        val loremIpsumFieldDataProvider = mock<LoremIpsumFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getLoremIpsumFieldDataProvider(20, 22, "\n")).willReturn(loremIpsumFieldDataProvider as (TestPerson?) -> String)
        val expectedValue = "Lorem ipsum and stuff"
        given(loremIpsumFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("biography")
            .returningLoremIpsum(20, 22, "\n")
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue, testPerson.biography)
    }

    @Test
    fun `Given builder returning Enum when generate then instance has correct value`() {
        // Given
        val randomEnumFieldDataProvider = mock<RandomEnumFieldDataProvider<TestPerson, Gender>>()
        given(fieldDataProviderFactory.getRandomEnumFieldDataProvider(Gender::class.java)).willReturn(randomEnumFieldDataProvider)
        given(randomEnumFieldDataProvider.invoke(any<TestPerson>())).willReturn(Gender.MALE)

        cut = incompleteBuilderField
            .withField("gender")
            .returning(Gender::class.java)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(Gender.MALE, testPerson.gender)
    }

    @Test
    fun `Given builder returning FieldDataProvider when generate then instance has correct value`() {
        // Given
        val fieldDataProvider = mock<(TestPerson?) -> String>()
        val expectedValue = "Inigo Montoya"
        given(fieldDataProvider.invoke(any())).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("name")
            .returning(fieldDataProvider)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue, testPerson.name)
    }

    @Test
    fun `Given builder returning RandomGen when generate then instance has correct value`() {
        // Given
        val randomGen = mock<RandomGen<String>>()
        val expectedValue = "Inigo Montoya"
        given(randomGen.invoke(any(TestPerson::class.java))).willReturn(expectedValue)

        cut = incompleteBuilderField
            .withField("name")
            .returning(randomGen)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValue, testPerson.name)
    }

    @Test
    fun `Given builder returning custom ListFieldDataProvider instances when generate then instance has correct value`() {
        // Given
        val fieldDataProvider = mock<(TestPerson?) -> String>()
        val customListFieldDataProvider = mock<CustomListFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getCustomListFieldDataProvider(3, fieldDataProvider)).willReturn(customListFieldDataProvider)
        val expectedValues = Arrays.asList("The Shadow", "Captain Hammer", "Mr. Nobody")
        given(customListFieldDataProvider.invoke(any())).willReturn(expectedValues)

        cut = incompleteBuilderField
            .withField("aliases")
            .returning(3, fieldDataProvider)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValues, testPerson.aliases)
    }

    @Test
    fun `Given builder returning RandomGen instances when generate then instance has correct value`() {
        // Given
        val randomGen = mock<RandomGen<TestPerson>>() as (TestPerson?) -> String
        val customListFieldDataProvider = mock<CustomListFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getCustomListFieldDataProvider(3, randomGen)).willReturn(customListFieldDataProvider)
        val expectedValues = Arrays.asList("The Shadow", "Captain Hammer", "Mr. Nobody")
        given(customListFieldDataProvider.invoke(any())).willReturn(expectedValues)

        cut = incompleteBuilderField
            .withField("aliases")
            .returning(3, randomGen)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValues, testPerson.aliases)
    }

    @Test
    fun `Given builder returning range and custom ListFieldDataProvider when generate then instance has correct value`() {
        // Given
        val fieldDataProvider = mock<(TestPerson?) -> String>()
        val customListRangeFieldDataProvider = mock<(TestPerson?) -> List<String>>()
        given(fieldDataProviderFactory.getCustomListRangeFieldDataProvider(2, 4, fieldDataProvider)).willReturn(customListRangeFieldDataProvider)
        val expectedValues = Arrays.asList("The Shadow", "Captain Hammer", "Mr. Nobody")
        given(customListRangeFieldDataProvider.invoke(any())).willReturn(expectedValues)

        cut = incompleteBuilderField
            .withField("aliases")
            .returning(2, 4, fieldDataProvider)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValues, testPerson.aliases)
    }

    @Test
    fun `Given builder returning range and RandomGen when generate then instance has correct value`() {
        // Given
        val randomGen = mock<RandomGen<TestPerson>>() as (TestPerson?) -> String
        val customListRangeFieldDataProvider = mock<CustomListRangeFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getCustomListRangeFieldDataProvider(2, 4, randomGen)).willReturn(customListRangeFieldDataProvider)
        val expectedValues = Arrays.asList("The Shadow", "Captain Hammer", "Mr. Nobody")
        given(customListRangeFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValues)

        cut = incompleteBuilderField
            .withField("aliases")
            .returning(2, 4, randomGen)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        assertEquals(expectedValues, testPerson.aliases)
    }

    @Test
    fun `Given invalid value when generate then instance throws IllegalArgumentException`() {
        // Given
        val name = "Superman"

        val explicitFieldDataProvider = mock<ExplicitFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name)).willReturn(explicitFieldDataProvider)
        given(explicitFieldDataProvider.invoke(any())).willReturn(name)

        cut = incompleteBuilderField
            .withField("candiesCount")
            .returningExplicitly(name)
            .build()

        var caughtException: Throwable? = null

        try {
            // When
            cut.generate()
        } catch (exception: Throwable) {
            // Then
            caughtException = exception
        }

        assertTrue(caughtException is IllegalArgumentException)
        assertEquals("Cannot set field candiesCount due to invalid value", caughtException!!.message)
        assertEquals("java.lang.IllegalArgumentException: Can not set final int field com.mitteloupe.randomgenkt.RandomGenTest\$TestPerson.candiesCount to java.lang.String", caughtException.cause?.message)
    }

    @Test
    fun `Given non-list value for list field when generate then instance throws IllegalArgumentException`() {
        // Given
        val name = "Superman"

        val explicitFieldDataProvider = mock<ExplicitFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name)).willReturn(explicitFieldDataProvider)
        given(explicitFieldDataProvider.invoke(any<TestPerson>())).willReturn(name)

        cut = incompleteBuilderField
            .withField("bites")
            .returningExplicitly(name)
            .build()

        var caughtException: Throwable? = null

        try {
            // When
            cut.generate()
        } catch (exception: Throwable) {
            // Then
            caughtException = exception
        }

        assertTrue(caughtException is IllegalArgumentException)
        assertEquals("Cannot set field bites due to invalid value", caughtException!!.message)
        assertEquals("java.lang.RuntimeException: Expected collection value", caughtException.cause?.message)
    }

    @Test
    fun `Given invalid value for list when generate then instance throws IllegalArgumentException`() {
        // Given
        val namesArray = arrayOf("Superman")
        val namesList = namesArray.toList()

        val explicitFieldDataProvider = mock<ExplicitFieldDataProvider<TestPerson, List<String>>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(namesList)).willReturn(explicitFieldDataProvider)
        given(explicitFieldDataProvider.invoke(any<TestPerson>())).willReturn(namesList)

        cut = incompleteBuilderField
            .withField("bites")
            .returningExplicitly(namesList)
            .build()

        var caughtException: Throwable? = null

        try {
            // When
            cut.generate()
        } catch (exception: Throwable) {
            // Then
            caughtException = exception
        }

        assertTrue(caughtException is IllegalArgumentException)
        assertEquals("Cannot set field bites due to invalid value", caughtException!!.message)
        assertEquals("java.lang.ArrayStoreException", caughtException.cause?.message)
    }

    @Test
    fun `Given non-existent field when generate then instance throws IllegalArgumentException`() {
        // Given
        val name = "Superman"

        val explicitFieldDataProvider = mock<ExplicitFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name)).willReturn(explicitFieldDataProvider)
        given(explicitFieldDataProvider.invoke(any<TestPerson>())).willReturn(name)

        cut = incompleteBuilderField
            .withField("unknownField")
            .returningExplicitly(name)
            .build()

        var caughtException: Throwable? = null

        try {
            // When
            cut.generate()
        } catch (exception: Throwable) {
            // Then
            caughtException = exception
        }

        assertTrue(caughtException is IllegalArgumentException)
        assertEquals("Cannot set field unknownField - field not found", caughtException!!.message)
    }

    @Test
    fun `Given builder with onGenerate set when generate then onGenerate called`() {
        // Given
        val onGenerateCallback = mock<RandomGen.OnGenerateCallback<TestPerson>>()

        // Minimal requirement for instance creation is one value.
        val explicitFieldDataProvider = mock<ExplicitFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider("")).willReturn(explicitFieldDataProvider)

        cut = incompleteBuilderField
            .onGenerate(onGenerateCallback)
            .withField("name")
            .returningExplicitly("")
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        verify<RandomGen.OnGenerateCallback<TestPerson>>(onGenerateCallback).onGenerate(testPerson)
    }

    @Test
    fun `Given builder returning explicit value or another when generate then second provider added and instance has correct value`() {
        // Given
        val name1 = "Superman"
        val name2 = "Spider Man"

        val explicitFieldDataProvider1 = mock<ExplicitFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name1)).willReturn(explicitFieldDataProvider1)
        val explicitFieldDataProvider2 = mock<ExplicitFieldDataProvider<Any, Nothing>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name2)).willReturn(explicitFieldDataProvider2)

        val weightedFieldDataProvidersFieldDataProvider = mock<WeightedFieldDataProvidersFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getWeightedFieldDataProvidersFieldDataProvider(explicitFieldDataProvider1))
            .willReturn(weightedFieldDataProvidersFieldDataProvider)
        given(weightedFieldDataProvidersFieldDataProvider.invoke(any<TestPerson>()))
            .willReturn(name1)

        cut = incompleteBuilderField
            .withField("name")
            .returningExplicitly(name1)
            .or()
            .returningExplicitly(name2)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        verify(weightedFieldDataProvidersFieldDataProvider).addFieldDataProvider(explicitFieldDataProvider2, 1.0)
        assertEquals(name1, testPerson.name)
    }

    @Test
    fun `Given builder returning explicit value or another with weight when generate then second provider added and instance has correct value`() {
        // Given
        val name1 = "Superman"
        val name2 = "Spider Man"
        val weight = 2.0

        val explicitFieldDataProvider1 = mock<ExplicitFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name1)).willReturn(explicitFieldDataProvider1)
        val explicitFieldDataProvider2 = mock<ExplicitFieldDataProvider<Any, Nothing>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name2)).willReturn(explicitFieldDataProvider2)

        val weightedFieldDataProvidersFieldDataProvider = mock<WeightedFieldDataProvidersFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getWeightedFieldDataProvidersFieldDataProvider(explicitFieldDataProvider1))
            .willReturn(weightedFieldDataProvidersFieldDataProvider)
        given(weightedFieldDataProvidersFieldDataProvider.invoke(any()))
            .willReturn(name1)

        cut = incompleteBuilderField
            .withField("name")
            .returningExplicitly(name1)
            .orWithWeight(weight)
            .returningExplicitly(name2)
            .build()

        // When
        val testPerson = cut.generate()

        // Then
        verify(weightedFieldDataProvidersFieldDataProvider).addFieldDataProvider(explicitFieldDataProvider2, weight)
        assertEquals(name1, testPerson.name)
    }

    // Setting is done via RandomGen :)
    class TestPerson {
        internal val id: Int = 0
        internal val name: String? = null
        internal val aliases: List<String>? = null
        internal val isBrave: Boolean = false
        internal val bite: Byte = 0 // Everybody gets hungry sometimes!
        internal val bites: Array<Byte>? = null // Sometimes you get even hungrier!
        internal val wealth: Double = 0.toDouble()
        internal val height: Float = 0.toFloat()
        internal val candiesCount: Int = 0 // Like taking candies from a baby!
        internal val soLong: Long = 0
        internal val uuid: String? = null
        internal val shirtColor: String? = null
        internal val biography: String? = null
        internal val gender: Gender? = null
        internal val isLazy by lazy { true }
    }

    enum class Gender {
        MALE, FEMALE
    }
}