package com.mitteloupe.randomgenkt.fielddataprovider

import java.util.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RgbFieldDataProviderTest {
    private lateinit var classUnderTest: RgbFieldDataProvider<Any>

    @Mock
    private lateinit var random: Random

    @Test
    fun givenARandomGeneratorAndAlphaWhenGenerateThenReturnsRGBAValue() {
        // Given
        classUnderTest = RgbFieldDataProvider(random, true)
        givenRandomValues(173, 250, 17, 222)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertEquals("#deadfa11", result)
    }

    @Test
    fun givenARandomGeneratorAndNoAlphaWhenGenerateThenReturnsRGBValue() {
        // Given
        classUnderTest = RgbFieldDataProvider(random, false)
        givenRandomValues(176, 85, 237)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertEquals("#b055ed", result)
    }

    private fun givenRandomValues(value: Int, vararg moreValues: Int) {
        given(random.nextInt(255)).willReturn(value, *moreValues.toTypedArray())
    }
}
