package com.mitteloupe.randomgenktexample.presentation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mitteloupe.randomgenktexample.data.model.flat.Flat
import com.mitteloupe.randomgenktexample.data.model.person.Person
import com.mitteloupe.randomgenktexample.data.model.planet.PlanetarySystem
import com.mitteloupe.randomgenktexample.domain.GenerateFlatUseCase
import com.mitteloupe.randomgenktexample.domain.GeneratePersonUseCase
import com.mitteloupe.randomgenktexample.domain.GeneratePlanetarySystemUseCase
import com.mitteloupe.randomgenktexample.domain.UseCaseExecutor
import javax.inject.Inject

class MainViewModel(
    private val useCaseExecutor: UseCaseExecutor,
    private val generatePersonUseCase: dagger.Lazy<GeneratePersonUseCase>,
    private val generatePlanetarySystemUseCase: dagger.Lazy<GeneratePlanetarySystemUseCase>,
    private val generateFlatUseCase: dagger.Lazy<GenerateFlatUseCase>
) : ViewModel() {
    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState>
        get() = _viewState

    private val people = mutableListOf<Person>()

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
        useCaseExecutor.execute(generatePersonUseCase.get()) { person ->
            people.add(0, person)
            _viewState.value = ViewState.ShowPeople(people)
        }

    private fun generatePlanetarySystem() =
        useCaseExecutor.execute(generatePlanetarySystemUseCase.get()) { planetarySystem ->
            _viewState.value = ViewState.ShowPlanetarySystem(planetarySystem)
        }

    private fun generateFlat() =
        useCaseExecutor.execute(generateFlatUseCase.get()) { flat ->
            _viewState.value = ViewState.ShowFlat(flat)
        }
}

class MainViewModelFactory @Inject constructor(
    private val useCaseExecutor: UseCaseExecutor,
    private val generatePersonUseCase: dagger.Lazy<GeneratePersonUseCase>,
    private val generatePlanetarySystemUseCase: dagger.Lazy<GeneratePlanetarySystemUseCase>,
    private val generateFlatUseCase: dagger.Lazy<GenerateFlatUseCase>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(
            useCaseExecutor,
            generatePersonUseCase,
            generatePlanetarySystemUseCase,
            generateFlatUseCase
        ) as T
    }
}

sealed class ViewState {
    data class ShowPeople(val people: List<Person>) : ViewState()
    data class ShowPlanetarySystem(val planetarySystem: PlanetarySystem) : ViewState()
    data class ShowFlat(val flat: Flat) : ViewState()
    object ShowNone : ViewState()
}
