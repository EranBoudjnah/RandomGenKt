package com.mitteloupe.randomgenkt.fielddataprovider

import java.util.Random
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@RunWith(Parameterized::class)
class RandomEnumFieldDataProviderTest(
    private val randomValue: Int,
    private val expectedColor: MagicColors?
) {
    companion object {
        @JvmStatic
        @Parameters
        fun data(): Collection<Array<*>> = listOf(
            arrayOf<Any>(0, MagicColors.WHITE),
            arrayOf<Any>(2, MagicColors.BLACK),
            arrayOf<Any>(4, MagicColors.GREEN)
        )
    }

    private lateinit var classUnderTest: RandomEnumFieldDataProvider<Any, MagicColors>

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var random: Random

    @Before
    fun setUp() {
        classUnderTest = RandomEnumFieldDataProvider(random, MagicColors::class.java)
    }

    @Test
    fun givenRandomIntValueWhenGenerateThenReturnsIntegerValue() {
        // Given
        given(random.nextInt(5)).willReturn(randomValue)

        // When
        val result = classUnderTest()

        // Then
        assertEquals(expectedColor, result)
    }

    enum class MagicColors {
        WHITE,
        BLUE,
        BLACK,
        RED,
        GREEN
    }
}
