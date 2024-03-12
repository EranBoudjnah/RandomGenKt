package com.mitteloupe.randomgenkt

import com.mitteloupe.randomgenkt.fielddataprovider.RgbFieldDataProvider
import java.util.Random
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.given
import org.mockito.kotlin.isNull
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class SimpleFieldDataProviderFactoryTest {
    private lateinit var classUnderTest: SimpleFieldDataProviderFactory<Any>

    @Mock
    private lateinit var random: Random

    @Mock
    private lateinit var uuidGenerator: UuidGenerator

    @Before
    fun setUp() {
        classUnderTest = SimpleFieldDataProviderFactory(random, uuidGenerator)
    }

    @Test
    fun `Given explicit FieldDataProvider when invoked then returns explicit value`() {
        // Given
        val expectedInstance = "Popcorn"
        val dataProvider = classUnderTest.getExplicitFieldDataProvider(expectedInstance)

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedInstance, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given generic List FieldDataProvider, list of values, minimum random when invoked then returns first value`() {
        // Given
        val expectedInstance = "A"
        val givenInstances = listOf(expectedInstance, "B")
        val dataProvider = classUnderTest.getGenericListFieldDataProvider(givenInstances)

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedInstance, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given generic List FieldDataProvider, list of values, maximum random when invoked then returns last value`() {
        // Given
        val expectedInstance = "C"
        val givenInstances = listOf("A", "B", expectedInstance)
        val dataProvider = classUnderTest.getGenericListFieldDataProvider(givenInstances)
        given { random.nextInt(givenInstances.size) }.willReturn(givenInstances.size - 1)

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedInstance, actualValue)
    }

    @Test
    fun `Given Boolean FieldDataProvider, false random when invoked then returns false`() {
        // Given
        val dataProvider = classUnderTest.booleanFieldDataProvider
        given { random.nextBoolean() }.willReturn(false)

        // When
        val actualValue = dataProvider()

        // Then
        assertFalse(actualValue)
    }

    @Test
    fun `Given Boolean FieldDataProvider, true random when invoked then returns true`() {
        // Given
        val dataProvider = classUnderTest.booleanFieldDataProvider
        given { random.nextBoolean() }.willReturn(true)

        // When
        val actualValue = dataProvider()

        // Then
        assertTrue(actualValue)
    }

    @Test
    fun `Given Byte FieldDataProvider when invoked then returns expected Byte`() {
        // Given
        val dataProvider = classUnderTest.byteFieldDataProvider
        val expectedInstance = Byte.MAX_VALUE
        given { random.nextBytes(any()) }.willAnswer { invocation ->
            val byteArray: ByteArray = invocation.getArgument(0)
            byteArray[0] = expectedInstance
            Unit
        }

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedInstance, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given ByteArray FieldDataProvider, fixed size when invoked then returns fixed number of random Bytes`() {
        // Given
        val givenSize = 4
        val dataProvider = classUnderTest.getByteArrayFieldDataProvider(givenSize)
        val expectedValue = listOf(4.toByte(), 6.toByte(), 8.toByte(), 10.toByte()).toByteArray()
        given { random.nextBytes(any()) }.willAnswer { invocation ->
            val byteArray: ByteArray = invocation.getArgument(0)
            repeat(4) { index ->
                byteArray[index] = expectedValue[index]
            }
        }

        // When
        val actualValue = dataProvider()

        // Then
        assertArrayEquals(expectedValue, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given ByteArray FieldDataProvider, ranged size, minimum random when invoked then returns shortest ByteArray`() {
        // Given
        val minimumSize = 3
        val maximumSize = 5
        val dataProvider = classUnderTest.getByteArrayFieldDataProvider(minimumSize, maximumSize)
        val givenInstance = listOf(4.toByte(), 6.toByte(), 8.toByte(), 10.toByte(), 12.toByte())
        given { random.nextInt(3) }.willReturn(0)
        given { random.nextBytes(any()) }.willAnswer { invocation ->
            val byteArray: ByteArray = invocation.getArgument(0)
            repeat(5) { index ->
                byteArray[index] = givenInstance[index]
            }
        }
        val expectedValue = givenInstance.subList(0, minimumSize).toByteArray()

        // When
        val actualValue = dataProvider()

        // Then
        assertArrayEquals(expectedValue, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given ByteArray FieldDataProvider, ranged size, maximum random when invoked then returns longest ByteArray`() {
        // Given
        val minimumSize = 3
        val maximumSize = 5
        val dataProvider = classUnderTest.getByteArrayFieldDataProvider(minimumSize, maximumSize)
        given { random.nextInt(3) }.willReturn(2)
        val expectedValue = listOf(4.toByte(), 6.toByte(), 8.toByte(), 10.toByte(), 12.toByte())
            .toByteArray()
        given { random.nextBytes(any()) }.willAnswer { invocation ->
            val byteArray: ByteArray = invocation.getArgument(0)
            repeat(5) { index ->
                byteArray[index] = expectedValue[index]
            }
        }

        // When
        val actualValue = dataProvider()

        // Then
        assertArrayEquals(expectedValue, actualValue)
    }

    @Test
    fun `Given Double FieldDataProvider when invoked then returns expected value`() {
        // Given
        val dataProvider = classUnderTest.getDoubleFieldDataProvider()
        val expectedInstance = 0.7
        given { random.nextDouble() }.willReturn(expectedInstance)

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedInstance, actualValue, 0.000001)
    }

    @Test
    fun `Given Double FieldDataProvider, range when invoked then returns expected value`() {
        // Given
        val minValue = -3.0
        val maxValue = 3.0
        val dataProvider = classUnderTest.getDoubleFieldDataProvider(minValue, maxValue)
        given { random.nextDouble() }.willReturn(0.7)
        val expectedInstance = 1.2

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedInstance, actualValue, 0.000001)
    }

    @Test
    fun `Given Float FieldDataProvider when invoked then returns value between 0 and 1`() {
        // Given
        val dataProvider = classUnderTest.getFloatFieldDataProvider()
        val expectedInstance = 0.7f
        given { random.nextFloat() }.willReturn(expectedInstance)

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedInstance, actualValue, 0.00001f)
    }

    @Test
    fun `Given Float FieldDataProvider, range when invoked then returns expected value`() {
        // Given
        val minimumValue = -3f
        val maximumValue = 3f
        val dataProvider = classUnderTest.getFloatFieldDataProvider(minimumValue, maximumValue)
        given { random.nextFloat() }.willReturn(0.7f)
        val expectedInstance = 1.2f

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedInstance, actualValue, 0.00001f)
    }

    @Test
    fun `Given Int FieldDataProvider then returns maximal value`() {
        // Given
        val dataProvider = classUnderTest.getIntFieldDataProvider()
        given { random.nextDouble() }.willReturn(0.99999999999)
        val expectedInstance = Int.MAX_VALUE

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedInstance, actualValue)
    }

    @Test
    fun `Given Int FieldDataProvider, range then returns maximal value`() {
        // Given
        val minimumValue = -3
        val maximumValue = 3
        val dataProvider = classUnderTest.getIntFieldDataProvider(minimumValue, maximumValue)
        given { random.nextDouble() }.willReturn(0.99999999999)

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(maximumValue, actualValue)
    }

    @Test
    fun `Given Long FieldDataProvider when invoked then returns maximal value`() {
        // Given
        val dataProvider = classUnderTest.getLongFieldDataProvider()
        given { random.nextDouble() }.willReturn(0.9999999999999999)
        val expectedInstance = Long.MAX_VALUE

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedInstance, actualValue)
    }

    @Test
    fun `Given Long FieldDataProvider, range when invoked then returns maximal value`() {
        // Given
        val minimalValue = -3L
        val maximalValue = 3L
        val dataProvider = classUnderTest.getLongFieldDataProvider(minimalValue, maximalValue)
        given { random.nextDouble() }.willReturn(0.9999999999999999)

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(maximalValue, actualValue)
    }

    @Test
    fun `Given sequential Integer FieldDataProvider invoked when invoked again then returns 2`() {
        // Given
        val dataProvider = classUnderTest.sequentialIntegerFieldDataProvider
        dataProvider()

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(2, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given sequential Integer FieldDataProvider, initial value when invoked then returns initial value`() {
        // Given
        val startValue = 42
        val dataProvider = classUnderTest.getSequentialIntegerFieldDataProvider(startValue)

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(startValue, actualValue)
    }

    @Test
    fun `Given UUID FieldDataProvider, UuidGenerator when invoked then returns generated UUID`() {
        // Given
        val dataProvider = classUnderTest.uuidFieldDataProvider
        val expectedUUID = "SOME-UUID"
        given { uuidGenerator.randomUUID() }.willReturn(expectedUUID)

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedUUID, actualValue)
    }

    @Test
    fun `Given rgbFieldDataProvider, alpha when invoked then returns random ARGB value`() {
        // Given
        val dataProvider: RgbFieldDataProvider<*> = classUnderTest.getRgbFieldDataProvider(true)
        val expectedValue = "#00000000"

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Given rgbFieldDataProvider, no alpha when invoked then returns random RGB value`() {
        // Given
        val dataProvider = classUnderTest.getRgbFieldDataProvider(false)
        val expectedValue = "#000000"

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Given Lorem Ipsum FieldDataProvider when invoked then returns whole Lorem Ipsum`() {
        // Given
        val dataProvider = classUnderTest.loremIpsumFieldDataProvider
        val expectedValue =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo " +
                "consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse " +
                "cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non " +
                "proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedValue, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given Lorem Ipsum FieldDataProvider, length when invoked then returns fixed length Lorem Ipsum`() {
        // Given
        val length = 63
        val dataProvider = classUnderTest.getLoremIpsumFieldDataProvider(length)
        val expectedValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do"

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedValue, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given Lorem Ipsum FieldDataProvider, ranged length, minimum random when invoked then returns minimal length Lorem Ipsum`() {
        // Given
        val minimumLength = 60
        val maximumLength = 375
        val dataProvider = classUnderTest.getLoremIpsumFieldDataProvider(
            minimumLength = minimumLength,
            maximumLength = maximumLength
        )
        given { random.nextInt(maximumLength - minimumLength + 1) }.willReturn(0)
        val expectedValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed"

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedValue, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given Lorem Ipsum FieldDataProvider, ranged length, maximum random when invoked then returns maximal length, default delimiter Lorem Ipsum`() {
        // Given
        val minimumLength = 60
        val maximumLength = 448
        val dataProvider = classUnderTest.getLoremIpsumFieldDataProvider(
            minimumLength = minimumLength,
            maximumLength = maximumLength
        )
        given {
            random.nextInt(maximumLength - minimumLength + 1)
        }.willReturn(maximumLength - minimumLength)
        val expectedValue =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo " +
                "consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse " +
                "cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non " +
                "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n\nL"

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedValue, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given Lorem Ipsum FieldDataProvider, range, length, delimiter, minimum random when invoked then returns expected Lorem Ipsum`() {
        // Given
        val minimumLength = 474
        val maximumLength = 480
        val paragraphDelimiter = "... "
        given { random.nextInt(7) }.willReturn(0)
        val expectedInstance = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
            "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
            "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo " +
            "consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
            "dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, " +
            "sunt in culpa qui officia deserunt mollit anim id est laborum.... Lorem ipsum dolor " +
            "sit ame"
        val dataProvider = classUnderTest.getLoremIpsumFieldDataProvider(
            minimumLength,
            maximumLength,
            paragraphDelimiter
        )

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedInstance, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given range, length, delimiter, maximum random when getLoremIpsumFieldDataProvider then returns instance with correct properties set`() {
        // Given
        val minLength = 474
        val maxLength = 480
        given { random.nextInt(7) }.willReturn(6)
        val paragraphDelimiter = "||| "
        val expectedInstance = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
            "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
            "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo " +
            "consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
            "dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, " +
            "sunt in culpa qui officia deserunt mollit anim id est laborum.||| Lorem ipsum dolor " +
            "sit amet, con"

        // When
        val dataProvider =
            classUnderTest.getLoremIpsumFieldDataProvider(minLength, maxLength, paragraphDelimiter)
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedInstance, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given random Enum FieldDataProvider, minimum random when invoked then returns first value`() {
        // Given
        val dataProvider = classUnderTest.getRandomEnumFieldDataProvider(Rings::class.java)
        given { random.nextInt(Rings.entries.size) }.willReturn(0)
        val expectedValue = Rings.ONE_RING_TO_RULE_THEM_ALL

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedValue, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given random Enum FieldDataProvider, maximum random when invoked then returns last value`() {
        // Given
        val dataProvider = classUnderTest.getRandomEnumFieldDataProvider(Rings::class.java)
        val enumValuesCount = Rings.entries.size
        given { random.nextInt(enumValuesCount) }.willReturn(enumValuesCount - 1)
        val expectedValue = Rings.ONE_RING_TO_BRING_THEM_ALL

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedValue, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given paddedFieldDataProvider, fieldDataProvider, padding, length when invoked then returns minimal string`() {
        // Given
        val paddingString = "00"
        val minimumLength = 10
        val provider: FieldDataProvider<Any?, String> = object : FieldDataProvider<Any?, String>() {
            override fun invoke(instance: Any?): String = "123"
        }
        val dataProvider =
            classUnderTest.getPaddedFieldDataProvider(provider, minimumLength, paddingString)
        val expectedValue = "0000000123"

        // When
        val actualValue = dataProvider()

        // Then
        assertEquals(expectedValue, actualValue)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given instances count and fieldDataProvider when getCustomListFieldDataProvider then returns instance with correct properties set`() {
        // Given
        val expectedInstance = "Test"
        val provider: FieldDataProvider<Any?, String> = givenFieldDataProvider(expectedInstance)
        val expectedInstancesCount = 5
        val expectedInstances = List(expectedInstancesCount) { expectedInstance }

        // When
        val dataProvider = classUnderTest.getCustomListFieldDataProvider(
            provider,
            expectedInstancesCount
        )
        val actualValues = dataProvider()

        // Then
        assertEquals(expectedInstances, actualValues)
        assertThat(actualValues, `is`(equalTo(expectedInstances)))
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun `Given instances count, range and fieldDataProvider when getCustomListRangeFieldDataProvider then returns instance with correct properties set`() {
        // Given
        val expectedInstance = "Testing"
        val provider: FieldDataProvider<Any?, String> = givenFieldDataProvider(expectedInstance)
        given {
            @Suppress("KotlinConstantConditions")
            random.nextInt(1)
        }.willReturn(0)
        val minimumInstances = 5
        val maximumInstances = 5
        val expectedInstancesCount = 5
        val expectedInstances = List(expectedInstancesCount) { expectedInstance }

        // When
        val dataProvider = classUnderTest.getCustomListFieldDataProvider(
            provider,
            minimumInstances = minimumInstances,
            maximumInstances = maximumInstances
        )
        val actualValues = dataProvider()

        // Then
        assertThat(actualValues, `is`(equalTo(expectedInstances)))
    }

    private fun givenFieldDataProvider(expectedInstance: String): FieldDataProvider<Any?, String> {
        val provider: FieldDataProvider<Any?, String> = mock {
            on { invoke(isNull()) } doReturn expectedInstance
        }
        return provider
    }

    private enum class Rings {
        ONE_RING_TO_RULE_THEM_ALL,
        ONE_RING_TO_FIND_THEM,
        ONE_RING_TO_BRING_THEM_ALL
    }
}
