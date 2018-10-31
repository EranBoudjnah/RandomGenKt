package com.mitteloupe.randomgenexample.presentation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mitteloupe.randomgenexample.data.model.flat.Flat
import com.mitteloupe.randomgenexample.data.model.person.Person
import com.mitteloupe.randomgenexample.data.model.planet.PlanetarySystem
import com.mitteloupe.randomgenexample.domain.GenerateFlatUseCase
import com.mitteloupe.randomgenexample.domain.GeneratePersonUseCase
import com.mitteloupe.randomgenexample.domain.GeneratePlanetarySystemUseCase
import com.mitteloupe.randomgenexample.domain.UseCaseExecutor

/**
 * Created by Eran Boudjnah on 29/10/2018.
 */
class MainViewModel(
	private val useCaseExecutor: UseCaseExecutor,
	private val generatePersonUseCase: GeneratePersonUseCase,
	private val generatePlanetarySystemUseCase: GeneratePlanetarySystemUseCase,
	private val generateFlatUseCase: GenerateFlatUseCase
) : ViewModel() {
	private val _viewState = MutableLiveData<ViewState>()
	val viewState: LiveData<ViewState>
		get() = _viewState

	init {
		_viewState.value = ViewState.ShowNone
	}

	@VisibleForTesting
	public override fun onCleared() {
		super.onCleared()

		useCaseExecutor.abortAll()
	}

	fun onGeneratePersonClick() =
		generatePerson()

	fun onGeneratePlanetarySystemClick() =
		generatePlanetarySystem()

	fun onGenerateFlatClick() =
		generateFlat()

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