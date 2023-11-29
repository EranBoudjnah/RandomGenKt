package com.mitteloupe.randomgenkt

import com.mitteloupe.randomgenkt.KotlinDefaultValuesInstanceProvider.InstanceCreationException
import com.mitteloupe.randomgenkt.builder.IncompleteBuilderField
import com.mitteloupe.randomgenkt.builder.RandomGenBuilder
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
import com.mitteloupe.randomgenkt.fielddataprovider.ShortFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.UuidFieldDataProvider
import com.mitteloupe.randomgenkt.fielddataprovider.WeightedFieldDataProvidersFieldDataProvider
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.kotlin.mock

@RunWith(Parameterized::class)
class RandomGenTest(
    @Suppress("unused") private val testCase: String,
    private val incompleteBuilderField: IncompleteBuilderField<TestPerson>,
    private val fieldDataProviderFactory: FieldDataProviderFactory<TestPerson>
) {

    companion object {
        private const val EQUALS_PRECISION = 0.0001

        @JvmStatic
        @Parameters(name = "Given {0}")
        fun data(): Collection<Array<*>> {
            val factory: FieldDataProviderFactory<TestPerson> = mock()

            return listOf(
                factoryTestCase(factory),
                javaBuilderTestCase(factory),
                kotlinBuilderTestCase(factory)
            )
        }

        private fun factoryTestCase(factory: FieldDataProviderFactory<TestPerson>): Array<*> {
            val incompleteBuilderFieldWithFieldFactoryAndProvider = RandomGenBuilder<TestPerson>()
                .withFieldFactoryAndProvider(factory) { TestPerson() }

            return arrayOf("Factory", incompleteBuilderFieldWithFieldFactoryAndProvider, factory)
        }

        private fun javaBuilderTestCase(factory: FieldDataProviderFactory<TestPerson>): Array<*> {
            val incompleteBuilderFieldOfJavaClassAndFactory = RandomGenBuilder<TestPerson>()
                .ofKotlinClassWithFactory<TestPerson>(factory)

            return arrayOf("JavaBuilder", incompleteBuilderFieldOfJavaClassAndFactory, factory)
        }

        private fun kotlinBuilderTestCase(factory: FieldDataProviderFactory<TestPerson>): Array<*> {
            val incompleteBuilderFieldOfKotlinClassAndFactory = RandomGenBuilder<TestPerson>()
                .ofKotlinClassWithFactory<TestPerson>(factory)

            return arrayOf("KotlinBuilder", incompleteBuilderFieldOfKotlinClassAndFactory, factory)
        }
    }

    private lateinit var classUnderTest: RandomGen<TestPerson>

    @Before
    fun setUp() {
        reset(fieldDataProviderFactory)
    }

    @Test
    fun `Given builder returning explicitly when generate then instance has correct value`() {
        // Given
        val name = "Superman"

        val explicitFieldDataProvider = mock<ExplicitFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name)).willReturn(
            explicitFieldDataProvider
        )
        given(explicitFieldDataProvider.invoke(any<TestPerson>())).willReturn(name)

        classUnderTest = incompleteBuilderField
            .withField("name")
            .returningExplicitly(name)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(name, testPerson.name)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning first item from list when invoked then instance has first value`() {
        // Given
        val name1 = "Rocksteady"
        val name2 = "Bebop"

        val namesList = listOf(name1, name2)
        val genericListFieldDataProvider = mock<GenericListFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getGenericListFieldDataProvider(namesList))
            .willReturn(genericListFieldDataProvider)
        given(genericListFieldDataProvider(any<TestPerson>())).willReturn(name1)

        classUnderTest = incompleteBuilderField
            .withField("name")
            .returning(namesList)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(name1, testPerson.name)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning second item from list when invoked then instance has second value`() {
        // Given
        val name1 = "Rocksteady"
        val name2 = "Bebop"

        val namesList = listOf(name1, name2)
        val genericListFieldDataProvider = mock<GenericListFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getGenericListFieldDataProvider(namesList))
            .willReturn(genericListFieldDataProvider)
        given(genericListFieldDataProvider(any<TestPerson>())).willReturn(name2)

        classUnderTest = incompleteBuilderField
            .withField("name")
            .returning(namesList)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(name2, testPerson.name)
    }

    @Test
    fun `Given builder returning Boolean when generate then instance has correct value`() {
        // Given
        val booleanFieldDataProvider = mock<BooleanFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.booleanFieldDataProvider)
            .willReturn(booleanFieldDataProvider)
        given(booleanFieldDataProvider.invoke(any<TestPerson>())).willReturn(false)

        classUnderTest = incompleteBuilderField
            .withField("isBrave")
            .returningBoolean()
            .build()

        // When
        var testPerson = classUnderTest()

        // Then
        assertFalse(testPerson.isBrave)

        // Given
        given(booleanFieldDataProvider.invoke(any<TestPerson>())).willReturn(true)

        // When
        testPerson = classUnderTest()

        // Then
        assertTrue(testPerson.isBrave)
    }

    @Test
    fun `Given builder returning Byte when generate then instance has correct value`() {
        // Given
        val byteFieldDataProvider = mock<ByteFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.byteFieldDataProvider).willReturn(byteFieldDataProvider)
        given(byteFieldDataProvider.invoke(any<TestPerson>())).willReturn(3.toByte())

        classUnderTest = incompleteBuilderField
            .withField("byte")
            .returningByte()
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(3.toByte().toLong(), testPerson.byte.toLong())
    }

    @Test
    fun `Given builder returning Byte list when generate then instance has correct value`() {
        // Given
        val byteListFieldDataProvider = mock<ByteListFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getByteArrayFieldDataProvider(5, 5))
            .willReturn(byteListFieldDataProvider)
        val bytes = ByteArray(5) { index ->
            index.toByte()
        }
        given(byteListFieldDataProvider(any<TestPerson>())).willReturn(bytes)

        classUnderTest = incompleteBuilderField
            .withField("bytes")
            .returningBytes(5)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(bytes, testPerson.bytes)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning Byte list with range when generate then instance has correct value`() {
        // Given
        val byteListFieldDataProvider = mock<ByteListFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getByteArrayFieldDataProvider(4, 5))
            .willReturn(byteListFieldDataProvider)
        val bytes = ByteArray(5) { index ->
            index.toByte()
        }
        given(byteListFieldDataProvider(any<TestPerson>())).willReturn(bytes)

        classUnderTest = incompleteBuilderField
            .withField("bytes")
            .returningBytes(4, 5)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(bytes, testPerson.bytes)
    }

    @Test
    fun `Given builder returning Double when generate then instance has correct value`() {
        // Given
        val doubleFieldDataProvider = mock<DoubleFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getDoubleFieldDataProvider()).willReturn(
            doubleFieldDataProvider
        )
        val expectedValue = 1.2345
        given(doubleFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("wealth")
            .returningDouble()
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue, testPerson.wealth, 0.0001)
    }

    @Test
    fun `Given builder returning Double range when generate then instance has correct value`() {
        // Given
        val doubleRangeFieldDataProvider = mock<DoubleFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getDoubleFieldDataProvider(1.0, 2.0)).willReturn(
            doubleRangeFieldDataProvider
        )
        val expectedValue = 1.2345
        given(doubleRangeFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("wealth")
            .returning(1.0, 2.0)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue, testPerson.wealth, 0.0001)
    }

    @Test
    fun `Given builder returning Float when generate then instance has correct value`() {
        // Given
        val floatFieldDataProvider = mock<FloatFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getFloatFieldDataProvider()).willReturn(
            floatFieldDataProvider
        )
        val expectedValue = 1.23f
        given(floatFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("height")
            .returningFloat()
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue.toDouble(), testPerson.height.toDouble(), 0.0001)
    }

    @Test
    fun `Given builder returning Float range when generate then instance has correct value`() {
        // Given
        val floatRangeFieldDataProvider = mock<FloatFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getFloatFieldDataProvider(1f, 2f)).willReturn(
            floatRangeFieldDataProvider
        )
        val expectedValue = 1.23f
        given(floatRangeFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("height")
            .returning(1f, 2f)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue.toDouble(), testPerson.height.toDouble(), EQUALS_PRECISION)
    }

    @Test
    fun `Given builder returning Integer when generate then instance has correct value`() {
        // Given
        val integerFieldDataProvider = mock<IntFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getIntFieldDataProvider()).willReturn(
            integerFieldDataProvider
        )
        val expectedValue = 400
        given(integerFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("candiesCount")
            .returningInt()
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue.toLong(), testPerson.candiesCount.toLong())
    }

    @Test
    fun `Given builder returning Integer range when generate then instance has correct value`() {
        // Given
        val integerFieldDataProvider = mock<IntFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getIntFieldDataProvider(300, 500)).willReturn(
            integerFieldDataProvider
        )
        val expectedValue = 400
        given(integerFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("candiesCount")
            .returning(300, 500)
            .build()

        // When
        val testPerson = classUnderTest()

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

        classUnderTest = incompleteBuilderField
            .withField("soLong")
            .returningLong()
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue, testPerson.soLong)
    }

    @Test
    fun `Given builder returning Long range when generate then instance has correct value`() {
        // Given
        val longFieldDataProvider = mock<LongFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getLongFieldDataProvider(1000L, 2000L)).willReturn(
            longFieldDataProvider
        )
        val expectedValue = 1337L
        given(longFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("soLong")
            .returning(1000L, 2000L)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue, testPerson.soLong)
    }

    @Test
    fun `Given builder returning Short when generate then instance has correct value`() {
        // Given
        val shortFieldDataProvider = mock<ShortFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getShortFieldDataProvider()).willReturn(
            shortFieldDataProvider
        )
        val expectedValue = 400.toShort()
        given(shortFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("soShort")
            .returningShort()
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue, testPerson.soShort)
    }

    @Test
    fun `Given builder returning Short range when generate then instance has correct value`() {
        // Given
        val shortFieldDataProvider = mock<ShortFieldDataProvider<TestPerson>>()
        given(
            fieldDataProviderFactory.getShortFieldDataProvider(
                1000.toShort(),
                2000.toShort()
            )
        ).willReturn(shortFieldDataProvider)
        val expectedValue = 1337.toShort()
        given(shortFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("soShort")
            .returning(1000.toShort(), 2000.toShort())
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue, testPerson.soShort)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning sequential Integer when generate then instance has correct value`() {
        // Given
        val sequentialIntegerFieldDataProvider =
            mock<SequentialIntegerFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.sequentialIntegerFieldDataProvider)
            .willReturn(sequentialIntegerFieldDataProvider)
        val expectedValue = 1234567
        given(sequentialIntegerFieldDataProvider.invoke(any<TestPerson>()))
            .willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("id")
            .returningSequentialInteger()
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue.toLong(), testPerson.id.toLong())
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning sequential Integer with start value when generate then instance has correct value`() {
        // Given
        val sequentialIntegerFieldDataProvider =
            mock<SequentialIntegerFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getSequentialIntegerFieldDataProvider(5))
            .willReturn(sequentialIntegerFieldDataProvider)
        val expectedValue = 5
        given(sequentialIntegerFieldDataProvider.invoke(any<TestPerson>()))
            .willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("id")
            .returningSequentialInteger(5)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue.toLong(), testPerson.id.toLong())
    }

    @Test
    fun `Given builder returning UUID when generate then instance has correct value`() {
        // Given
        val uuidFieldDataProvider = mock<UuidFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.uuidFieldDataProvider)
            .willReturn(uuidFieldDataProvider as FieldDataProvider<TestPerson, String>)
        val expectedValue = "8b3728d0-9c1d-11e8-98d0-529269fb1459"
        given(uuidFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("uuid")
            .returningUuid()
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue, testPerson.uuid)
    }

    @Test
    fun `Given builder returning RGB when generate then instance has correct value`() {
        // Given
        val rgbFieldDataProvider = mock<RgbFieldDataProvider<TestPerson>>()
        val rgbaFieldDataProvider = mock<RgbFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getRgbFieldDataProvider(false))
            .willReturn(rgbFieldDataProvider as FieldDataProvider<TestPerson, String>)
        given(fieldDataProviderFactory.getRgbFieldDataProvider(true))
            .willReturn(rgbaFieldDataProvider as FieldDataProvider<TestPerson, String>)
        val expectedValueRGB = "#AABBAA"
        val expectedValueRGBA = "#FFAABBAA"
        given(rgbFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValueRGB)
        given(rgbaFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValueRGBA)

        classUnderTest = incompleteBuilderField
            .withField("shirtColor")
            .returningRgb(true)
            .build()

        // When
        var testPerson = classUnderTest()

        // Then
        assertEquals(expectedValueRGBA, testPerson.shirtColor)

        // Given
        classUnderTest = incompleteBuilderField
            .withField("shirtColor")
            .returningRgb(false)
            .build()

        // When
        testPerson = classUnderTest()

        // Then
        assertEquals(expectedValueRGB, testPerson.shirtColor)
    }

    @Test
    fun `Given builder returning Lorem Ipsum when generate then instance has correct value`() {
        // Given
        val loremIpsumFieldDataProvider = mock<LoremIpsumFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.loremIpsumFieldDataProvider).willReturn(
            loremIpsumFieldDataProvider as FieldDataProvider<TestPerson, String>
        )
        val expectedValue = "Lorem ipsum and stuff"
        given(loremIpsumFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("biography")
            .returningLoremIpsum()
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue, testPerson.biography)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning Lorem Ipsum with length when generate then instance has correct value`() {
        // Given
        val loremIpsumFieldDataProvider = mock<LoremIpsumFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getLoremIpsumFieldDataProvider(21)).willReturn(
            loremIpsumFieldDataProvider as FieldDataProvider<TestPerson, String>
        )
        val expectedValue = "Lorem ipsum and stuff"
        given(loremIpsumFieldDataProvider(any<TestPerson>())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("biography")
            .returningLoremIpsum(21)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue, testPerson.biography)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning Lorem Ipsum with length range when generate then instance has correct value`() {
        // Given
        val loremIpsumFieldDataProvider = mock<LoremIpsumFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getLoremIpsumFieldDataProvider(20, 22)).willReturn(
            loremIpsumFieldDataProvider as FieldDataProvider<TestPerson, String>
        )
        val expectedValue = "Lorem ipsum and stuff"
        given(loremIpsumFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("biography")
            .returningLoremIpsum(20, 22)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue, testPerson.biography)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning Lorem Ipsum with length range and delimiter when generate then instance has correct value`() {
        // Given
        val loremIpsumFieldDataProvider = mock<LoremIpsumFieldDataProvider<TestPerson>>()
        given(fieldDataProviderFactory.getLoremIpsumFieldDataProvider(20, 22, "\n")).willReturn(
            loremIpsumFieldDataProvider as FieldDataProvider<TestPerson, String>
        )
        val expectedValue = "Lorem ipsum and stuff"
        given(loremIpsumFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("biography")
            .returningLoremIpsum(20, 22, "\n")
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue, testPerson.biography)
    }

    @Test
    fun `Given builder returning Enum when generate then instance has correct value`() {
        // Given
        val randomEnumFieldDataProvider = mock<RandomEnumFieldDataProvider<TestPerson, Gender>>()
        given(fieldDataProviderFactory.getRandomEnumFieldDataProvider(Gender::class.java))
            .willReturn(randomEnumFieldDataProvider)
        given(randomEnumFieldDataProvider.invoke(any<TestPerson>())).willReturn(Gender.MALE)

        classUnderTest = incompleteBuilderField
            .withField("gender")
            .returning(Gender::class.java)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(Gender.MALE, testPerson.gender)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning FieldDataProvider when generate then instance has correct value`() {
        // Given
        val fieldDataProvider = mock<FieldDataProvider<TestPerson, String>>()
        val expectedValue = "Inigo Montoya"
        given(fieldDataProvider.invoke(any())).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("name")
            .returning(fieldDataProvider)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue, testPerson.name)
    }

    @Test
    fun `Given builder returning RandomGen when generate then instance has correct value`() {
        // Given
        val randomGen = mock<RandomGen<String>>()
        val expectedValue = "Inigo Montoya"
        given(randomGen.invoke(any(TestPerson::class.java))).willReturn(expectedValue)

        classUnderTest = incompleteBuilderField
            .withField("name")
            .returning(randomGen)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValue, testPerson.name)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning custom ListFieldDataProvider instances when generate then instance has correct value`() {
        // Given
        val fieldDataProvider = mock<FieldDataProvider<TestPerson, String>>()
        val customListFieldDataProvider = mock<CustomListFieldDataProvider<TestPerson, String>>()
        given(
            fieldDataProviderFactory
                .getCustomListFieldDataProvider(fieldDataProvider, minimumInstances = 3)
        ).willReturn(customListFieldDataProvider)
        val expectedValues = listOf("The Shadow", "Captain Hammer", "Mr. Nobody")
        given(customListFieldDataProvider.invoke(any())).willReturn(expectedValues)

        classUnderTest = incompleteBuilderField
            .withField("aliases")
            .returning(fieldDataProvider, 3)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValues, testPerson.aliases)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning RandomGen instances when generate then instance has correct value`() {
        // Given
        val randomGen = mock<FieldDataProvider<TestPerson, String>>()
        val customListFieldDataProvider = mock<CustomListFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getCustomListFieldDataProvider(randomGen, 3))
            .willReturn(customListFieldDataProvider)
        val expectedValues = listOf("The Shadow", "Captain Hammer", "Mr. Nobody")
        given(customListFieldDataProvider.invoke(any())).willReturn(expectedValues)

        classUnderTest = incompleteBuilderField
            .withField("aliases")
            .returning(randomGen, 3)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValues, testPerson.aliases)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning range and custom ListFieldDataProvider when generate then instance has correct value`() {
        // Given
        val fieldDataProvider: FieldDataProvider<TestPerson, String> = mock()
        val customListRangeFieldDataProvider: FieldDataProvider<TestPerson, List<String>> = mock()
        given(
            fieldDataProviderFactory.getCustomListFieldDataProvider(
                fieldDataProvider,
                minimumInstances = 2,
                maximumInstances = 4
            )
        ).willReturn(customListRangeFieldDataProvider)
        val expectedValues = listOf("The Shadow", "Captain Hammer", "Mr. Nobody")
        given(customListRangeFieldDataProvider.invoke(any())).willReturn(expectedValues)

        classUnderTest = incompleteBuilderField
            .withField("aliases")
            .returning(fieldDataProvider, minimumInstances = 2, maximumInstances = 4)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValues, testPerson.aliases)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning range and RandomGen when generate then instance has correct value`() {
        // Given
        val randomGen = mock<FieldDataProvider<TestPerson, String>>()
        val customListRangeFieldDataProvider =
            mock<CustomListRangeFieldDataProvider<TestPerson, String>>()
        given(
            fieldDataProviderFactory.getCustomListFieldDataProvider(
                randomGen,
                minimumInstances = 2,
                maximumInstances = 4
            )
        ).willReturn(customListRangeFieldDataProvider)
        val expectedValues = listOf("The Shadow", "Captain Hammer", "Mr. Nobody")
        given(customListRangeFieldDataProvider.invoke(any<TestPerson>())).willReturn(expectedValues)

        classUnderTest = incompleteBuilderField
            .withField("aliases")
            .returning(randomGen, minimumInstances = 2, maximumInstances = 4)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        assertEquals(expectedValues, testPerson.aliases)
    }

    @Test
    fun `Given invalid value when generate then instance throws IllegalArgumentException`() {
        // Given
        val name = "Superman"

        val explicitFieldDataProvider = mock<ExplicitFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name)).willReturn(
            explicitFieldDataProvider
        )
        given(explicitFieldDataProvider.invoke(any())).willReturn(name)

        classUnderTest = incompleteBuilderField
            .withField("candiesCount")
            .returningExplicitly(name)
            .build()

        var caughtException: Throwable? = null

        try {
            // When
            classUnderTest()
        } catch (exception: Throwable) {
            // Then
            caughtException = exception
        }

        assertThat(
            caughtException,
            anyOf(
                `is`(instanceOf(RuntimeException::class.java)),
                `is`(instanceOf(InstanceCreationException::class.java))
            )
        )
        assertThat(
            caughtException!!.message,
            anyOf(
                `is`("Cannot set field candiesCount due to an invalid value"),
                `is`("Failed to instantiate TestPerson. Try providing a ValuesInstanceProvider.")
            )
        )
        assertThat(
            caughtException.cause?.message,
            anyOf(
                `is`("java.lang.RuntimeException: No usable public constructors found."),
                `is`(
                    "java.lang.IllegalArgumentException: Can not set final int field " +
                        "com.mitteloupe.randomgenkt.RandomGenTest\$TestPerson.candiesCount " +
                        "to java.lang.String"
                )
            )
        )
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given non-list value for list field when generate then instance throws IllegalArgumentException`() {
        // Given
        val name = "Superman"

        val explicitFieldDataProvider = mock<ExplicitFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name)).willReturn(
            explicitFieldDataProvider
        )
        given(explicitFieldDataProvider.invoke(any<TestPerson>())).willReturn(name)

        classUnderTest = incompleteBuilderField
            .withField("bytes")
            .returningExplicitly(name)
            .build()

        var caughtException: Throwable? = null

        try {
            // When
            classUnderTest()
        } catch (exception: Throwable) {
            // Then
            caughtException = exception
        }

        assertThat(
            caughtException,
            anyOf(
                `is`(instanceOf(RuntimeException::class.java)),
                `is`(instanceOf(InstanceCreationException::class.java))
            )
        )
        assertThat(
            caughtException!!.message,
            anyOf(
                `is`("Cannot set field bytes due to an invalid value"),
                `is`("Failed to instantiate TestPerson. Try providing a ValuesInstanceProvider.")
            )
        )
        assertThat(
            caughtException.cause?.message,
            anyOf(
                `is`("java.lang.RuntimeException: Expected collection value"),
                `is`("java.lang.RuntimeException: No usable public constructors found.")
            )
        )
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given invalid value for list when generate then instance throws IllegalArgumentException`() {
        // Given
        val namesArray = arrayOf("Superman")
        val namesList = namesArray.toList()

        val explicitFieldDataProvider = mock<ExplicitFieldDataProvider<TestPerson, List<String>>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(namesList)).willReturn(
            explicitFieldDataProvider
        )
        given(explicitFieldDataProvider.invoke(any<TestPerson>())).willReturn(namesList)

        classUnderTest = incompleteBuilderField
            .withField("bytes")
            .returningExplicitly(namesList)
            .build()

        var caughtException: Throwable? = null

        try {
            // When
            classUnderTest()
        } catch (exception: Throwable) {
            // Then
            caughtException = exception
        }

        assertThat(
            caughtException,
            anyOf(
                `is`(instanceOf(RuntimeException::class.java)),
                `is`(instanceOf(InstanceCreationException::class.java))
            )
        )
        assertThat(
            caughtException!!.message,
            anyOf(
                `is`("Cannot set field bytes due to an invalid value"),
                `is`("Failed to instantiate TestPerson. Try providing a ValuesInstanceProvider.")
            )
        )
        assertThat(
            caughtException.cause?.message,
            anyOf(
                `is`("java.lang.RuntimeException: No usable public constructors found."),
                `is`(
                    "java.lang.ClassCastException: class [B cannot be cast to class " +
                        "[Ljava.lang.Object; ([B and [Ljava.lang.Object; are in module java.base " +
                        "of loader 'bootstrap')"
                )
            )
        )
    }

    @Test
    fun `Given non-existent field when generate then instance throws IllegalArgumentException`() {
        // Given
        val name = "Superman"

        val explicitFieldDataProvider = mock<ExplicitFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name)).willReturn(
            explicitFieldDataProvider
        )
        given(explicitFieldDataProvider(any<TestPerson>())).willReturn(name)

        classUnderTest = incompleteBuilderField
            .withField("unknownField")
            .returningExplicitly(name)
            .build()

        var caughtException: Throwable? = null

        try {
            // When
            classUnderTest()
        } catch (exception: Throwable) {
            // Then
            caughtException = exception
        }

        assertThat(caughtException, `is`(instanceOf(IllegalArgumentException::class.java)))
        assertEquals("Field(s) not found: unknownField", caughtException!!.message)
    }

    @Test
    fun `Given builder with onGenerate set when generate then onGenerate called`() {
        // Given
        val onGenerateCallback = mock<RandomGen.OnGenerateCallback<TestPerson>>()

        val explicitFieldDataProvider = mock<ExplicitFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider("")).willReturn(
            explicitFieldDataProvider
        )

        classUnderTest = incompleteBuilderField
            .onGenerate(onGenerateCallback)
            .withField("name")
            .returningExplicitly("")
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        verify(onGenerateCallback).onGenerate(testPerson)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning explicit value or another when generate then second provider added and instance has correct value`() {
        // Given
        val name1 = "Superman"
        val name2 = "Spider Man"

        val explicitFieldDataProvider1 = mock<ExplicitFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name1)).willReturn(
            explicitFieldDataProvider1
        )
        val explicitFieldDataProvider2 = mock<ExplicitFieldDataProvider<Any, Nothing>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name2)).willReturn(
            explicitFieldDataProvider2
        )

        val weightedFieldDataProvidersFieldDataProvider =
            mock<WeightedFieldDataProvidersFieldDataProvider<TestPerson, String>>()
        given(
            fieldDataProviderFactory.getWeightedFieldDataProvidersFieldDataProvider(
                explicitFieldDataProvider1
            )
        )
            .willReturn(weightedFieldDataProvidersFieldDataProvider)
        given(weightedFieldDataProvidersFieldDataProvider.invoke(any<TestPerson>()))
            .willReturn(name1)

        classUnderTest = incompleteBuilderField
            .withField("name")
            .returningExplicitly(name1)
            .or()
            .returningExplicitly(name2)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        verify(weightedFieldDataProvidersFieldDataProvider).addFieldDataProvider(
            explicitFieldDataProvider2,
            1.0
        )
        assertEquals(name1, testPerson.name)
    }

    @Test
    @Suppress("ktlint:max-line-length")
    fun `Given builder returning explicit value or another with weight when generate then second provider added and instance has correct value`() {
        // Given
        val name1 = "Superman"
        val name2 = "Spider Man"
        val weight = 2.0

        val explicitFieldDataProvider1 = mock<ExplicitFieldDataProvider<TestPerson, String>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name1)).willReturn(
            explicitFieldDataProvider1
        )
        val explicitFieldDataProvider2 = mock<ExplicitFieldDataProvider<Any, Nothing>>()
        given(fieldDataProviderFactory.getExplicitFieldDataProvider(name2)).willReturn(
            explicitFieldDataProvider2
        )

        val weightedFieldDataProvidersFieldDataProvider =
            mock<WeightedFieldDataProvidersFieldDataProvider<TestPerson, String>>()
        given(
            fieldDataProviderFactory.getWeightedFieldDataProvidersFieldDataProvider(
                explicitFieldDataProvider1
            )
        )
            .willReturn(weightedFieldDataProvidersFieldDataProvider)
        given(weightedFieldDataProvidersFieldDataProvider.invoke(any()))
            .willReturn(name1)

        classUnderTest = incompleteBuilderField
            .withField("name")
            .returningExplicitly(name1)
            .orWithWeight(weight)
            .returningExplicitly(name2)
            .build()

        // When
        val testPerson = classUnderTest()

        // Then
        verify(weightedFieldDataProvidersFieldDataProvider).addFieldDataProvider(
            explicitFieldDataProvider2,
            weight
        )
        assertEquals(name1, testPerson.name)
    }

    class TestPerson(
        internal val id: Int = 0,
        internal val name: String? = null,
        internal val aliases: List<String>? = null,
        internal val isBrave: Boolean = false,
        internal val byte: Byte = 0,
        internal val bytes: ByteArray? = null,
        internal val wealth: Double = 0.0,
        internal val height: Float = 0f,
        internal val candiesCount: Int = 0,
        internal val soLong: Long = 0,
        internal val soShort: Short = 0,
        internal val uuid: String? = null,
        internal val shirtColor: String? = null,
        internal val biography: String? = null,
        internal val gender: Gender? = null,
        constructorAndField: Int = 0
    ) {
        internal val constructorAndField: String = constructorAndField.toString()
    }

    enum class Gender {
        MALE, FEMALE
    }
}
