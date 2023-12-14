package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.UuidGenerator
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UuidFieldDataProviderTest {
    private lateinit var classUnderTest: UuidFieldDataProvider<Any>

    @Mock
    private lateinit var uuidGenerator: UuidGenerator

    @Before
    fun setUp() {
        classUnderTest = UuidFieldDataProvider(uuidGenerator)
    }

    @Test
    fun givenAUuidWhenGenerateThenReturnsGeneratedValue() {
        // Given
        val expectedValue = "c9569006-9d99-11e8-98d0-529269fb1459"
        given(uuidGenerator.randomUUID()).willReturn(expectedValue)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertEquals(expectedValue, result)
    }
}
