package com.mitteloupe.randomgenexample.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mitteloupe.randomgenexample.data.model.flat.Flat
import com.mitteloupe.randomgenexample.data.model.person.Person
import com.mitteloupe.randomgenexample.data.model.planet.PlanetarySystem
import com.mitteloupe.randomgenexample.domain.GenerateFlatUseCase
import com.mitteloupe.randomgenexample.domain.GeneratePersonUseCase
import com.mitteloupe.randomgenexample.domain.GeneratePlanetarySystemUseCase
import com.mitteloupe.randomgenexample.domain.UseCaseExecutor
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


/**
 * Created by Eran Boudjnah on 31/10/2018.
 */
class MainViewModelTest {
	@get:Rule
	val rule: MockitoRule = MockitoJUnit.rule()

	@get:Rule
	val taskExecutorRule = InstantTaskExecutorRule()

	@Mock
	lateinit var useCaseExecutor: UseCaseExecutor
	@Mock
	lateinit var generatePersonUseCase: GeneratePersonUseCase
	@Mock
	lateinit var generatePlanetarySystemUseCase: GeneratePlanetarySystemUseCase
	@Mock
	lateinit var generateFlatUseCase: GenerateFlatUseCase

	private lateinit var cut: MainViewModel

	@Before
	fun setUp() {
		MockitoAnnotations.initMocks(this)

		cut = MainViewModel(useCaseExecutor, generatePersonUseCase, generatePlanetarySystemUseCase, generateFlatUseCase)
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
		assertEquals(ViewState.ShowPerson(person), cut.viewState.value)
	}

	@Test
	fun `When onGeneratePlanetarySystemClick then sets state to generated planetary system`() {
		// Given
		val planetarySystem = mock<PlanetarySystem>()
		val callbackCaptor = argumentCaptor<(PlanetarySystem) -> Unit>()

		// When
		cut.onGeneratePlanetarySystemClick()

		// Then
		verify(useCaseExecutor).execute(eq(generatePlanetarySystemUseCase), callbackCaptor.capture())
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