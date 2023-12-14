package com.mitteloupe.randomgenktexample.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mitteloupe.randomgenktexample.data.model.flat.Flat
import com.mitteloupe.randomgenktexample.data.model.person.Person
import com.mitteloupe.randomgenktexample.data.model.planet.PlanetarySystem
import com.mitteloupe.randomgenktexample.domain.GenerateFlatUseCase
import com.mitteloupe.randomgenktexample.domain.GeneratePersonUseCase
import com.mitteloupe.randomgenktexample.domain.GeneratePlanetarySystemUseCase
import com.mitteloupe.randomgenktexample.domain.UseCaseExecutor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var useCaseExecutor: UseCaseExecutor

    @Mock
    lateinit var generatePersonUseCaseLazy: dagger.Lazy<GeneratePersonUseCase>

    @Mock
    lateinit var generatePersonUseCase: GeneratePersonUseCase

    @Mock
    lateinit var generatePlanetarySystemUseCaseLazy: dagger.Lazy<GeneratePlanetarySystemUseCase>

    @Mock
    lateinit var generatePlanetarySystemUseCase: GeneratePlanetarySystemUseCase

    @Mock
    lateinit var generateFlatUseCaseLazy: dagger.Lazy<GenerateFlatUseCase>

    @Mock
    lateinit var generateFlatUseCase: GenerateFlatUseCase

    private lateinit var cut: MainViewModel

    @Before
    fun setUp() {
        given(generatePersonUseCaseLazy.get()).willReturn(generatePersonUseCase)
        given(generatePlanetarySystemUseCaseLazy.get()).willReturn(generatePlanetarySystemUseCase)
        given(generateFlatUseCaseLazy.get()).willReturn(generateFlatUseCase)

        cut = MainViewModel(
            useCaseExecutor,
            generatePersonUseCaseLazy,
            generatePlanetarySystemUseCaseLazy,
            generateFlatUseCaseLazy
        )
    }

    @Test
    fun `When onGeneratePersonClick then sets state to generated person`() {
        // Given
        val person = mock<Person>()
        val callbackCaptor = argumentCaptor<(Person) -> Unit>()

        // When
        cut.onGeneratePersonClick()

        // Then
        verify(useCaseExecutor).execute(eq(generatePersonUseCase), callbackCaptor.capture())
        val callback = callbackCaptor.firstValue

        // When
        callback.invoke(person)

        // Then
        assertEquals(ViewState.ShowPeople(listOf(person)), cut.viewState.value)
    }

    @Test
    fun `When onGeneratePlanetarySystemClick then sets state to generated planetary system`() {
        // Given
        val planetarySystem = mock<PlanetarySystem>()
        val callbackCaptor = argumentCaptor<(PlanetarySystem) -> Unit>()

        // When
        cut.onGeneratePlanetarySystemClick()

        // Then
        verify(useCaseExecutor).execute(
            eq(generatePlanetarySystemUseCase),
            callbackCaptor.capture()
        )
        val callback = callbackCaptor.firstValue

        // When
        callback.invoke(planetarySystem)

        // Then
        assertEquals(ViewState.ShowPlanetarySystem(planetarySystem), cut.viewState.value)
    }

    @Test
    fun `When onGenerateFlatClick then sets state to generated flat`() {
        // Given
        val flat = mock<Flat>()
        val callbackCaptor = argumentCaptor<(Flat) -> Unit>()

        // When
        cut.onGenerateFlatClick()

        // Then
        verify(useCaseExecutor).execute(eq(generateFlatUseCase), callbackCaptor.capture())
        val callback = callbackCaptor.firstValue

        // When
        callback.invoke(flat)

        // Then
        assertEquals(ViewState.ShowFlat(flat), cut.viewState.value)
    }

    @Test
    fun `When clearing ViewModel use cases aborted`() {
        // When
        cut.onCleared()

        // Then
        verify(useCaseExecutor).abortAll()
    }
}
