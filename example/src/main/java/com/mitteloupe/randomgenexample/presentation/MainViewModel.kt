package com.mitteloupe.randomgenexample.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mitteloupe.randomgenexample.data.flat.Flat
import com.mitteloupe.randomgenexample.data.person.Person
import com.mitteloupe.randomgenexample.data.planet.PlanetarySystem
import com.mitteloupe.randomgenexample.domain.GenerateFlatUseCase
import com.mitteloupe.randomgenexample.domain.GeneratePersonUseCase
import com.mitteloupe.randomgenexample.domain.GeneratePlanetarySystemUseCase
import com.mitteloupe.randomgenexample.domain.UseCaseExecutor
import com.mitteloupe.randomgenexample.generator.FlatGeneratorFactory
import com.mitteloupe.randomgenexample.generator.PersonGeneratorFactory
import com.mitteloupe.randomgenexample.generator.PlanetarySystemGeneratorFactory
import java.util.Random

/**
 * Created by Eran Boudjnah on 29/10/2018.
 */
class MainViewModel : ViewModel() {
	private val _viewState = MutableLiveData<ViewState>()
	val viewState: LiveData<ViewState>
		get() = _viewState

	private lateinit var useCaseExecutor: UseCaseExecutor
	private lateinit var generatePersonUseCase: GeneratePersonUseCase
	private lateinit var generatePlanetarySystemUseCase: GeneratePlanetarySystemUseCase
	private lateinit var generateFlatUseCase: GenerateFlatUseCase

	init {
		initGenerators()

		_viewState.value = ViewState.ShowNone
	}

	override fun onCleared() {
		super.onCleared()

		useCaseExecutor.abortAll()
	}

	fun onGeneratePersonClick() =
		generatePerson()

	fun onGeneratePlanetarySystemClick() =
		generatePlanetarySystem()

	fun onGenerateFlatClick() =
		generateFlat()

	private fun initGenerators() {
		useCaseExecutor = UseCaseExecutor()

		generatePersonUseCase = GeneratePersonUseCase(PersonGeneratorFactory())
		generatePlanetarySystemUseCase = GeneratePlanetarySystemUseCase(PlanetarySystemGeneratorFactory())
		generateFlatUseCase = GenerateFlatUseCase(FlatGeneratorFactory(Random()))
	}

	private fun generatePerson() =
		useCaseExecutor.execute(generatePersonUseCase) { person ->
			_viewState.value = ViewState.ShowPerson(person)
		}

	private fun generatePlanetarySystem() =
		useCaseExecutor.execute(generatePlanetarySystemUseCase) { planetarySystem ->
			_viewState.value = ViewState.ShowPlanetarySystem(planetarySystem)
		}

	private fun generateFlat() =
		useCaseExecutor.execute(generateFlatUseCase) { flat ->
			_viewState.value = ViewState.ShowFlat(flat)
		}
}

sealed class ViewState {
	data class ShowPerson(val person: Person) : ViewState()
	data class ShowPlanetarySystem(val planetarySystem: PlanetarySystem) : ViewState()
	data class ShowFlat(val flat: Flat) : ViewState()
	object ShowNone : ViewState()
}