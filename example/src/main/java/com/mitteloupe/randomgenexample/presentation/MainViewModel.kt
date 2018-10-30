package com.mitteloupe.randomgenexample.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mitteloupe.randomgen.RandomGen
import com.mitteloupe.randomgenexample.data.flat.Flat
import com.mitteloupe.randomgenexample.data.person.Person
import com.mitteloupe.randomgenexample.data.planet.PlanetarySystem
import com.mitteloupe.randomgenexample.generator.FlatGeneratorFactory
import com.mitteloupe.randomgenexample.generator.PersonGeneratorFactory
import com.mitteloupe.randomgenexample.generator.PlanetarySystemGeneratorFactory
import java.util.Random

/**
 * Created by Eran Boudjnah on 29/10/2018.
 */
class MainViewModel: ViewModel() {
	private val _viewState = MutableLiveData<ViewState>()
	val viewState: LiveData<ViewState>
		get() = _viewState

	private lateinit var personGeneratorFactory: PersonGeneratorFactory
	private lateinit var personRandomGen: RandomGen<Person>
	private lateinit var planetarySystemGeneratorFactory: PlanetarySystemGeneratorFactory
	private lateinit var planetarySystemRandomGen: RandomGen<PlanetarySystem>
	private lateinit var flatGeneratorFactory: FlatGeneratorFactory
	private lateinit var flatRandomGen: RandomGen<Flat>

	init {
		initGenerators()

		_viewState.value = ViewState.ShowNone
	}

	fun onGeneratePersonClick() {
		_viewState.value = ViewState.ShowPerson(generatePerson())
	}

	fun onGeneratePlanetarySystemClick() {
		_viewState.value = ViewState.ShowPlanetarySystem(generatePlanetarySystem())
	}

	fun onGenerateFlatClick() {
		_viewState.value = ViewState.ShowFlat(generateFlat())
	}

	private fun initGenerators() {
		flatGeneratorFactory = FlatGeneratorFactory(Random())
		flatRandomGen = flatGeneratorFactory.newFlatGenerator

		personGeneratorFactory = PersonGeneratorFactory()
		personRandomGen = personGeneratorFactory.newPersonGenerator

		planetarySystemGeneratorFactory = PlanetarySystemGeneratorFactory()
		planetarySystemRandomGen = planetarySystemGeneratorFactory.newPlanetarySystemGenerator
	}

	private fun generatePerson() = personRandomGen.generate()

	private fun generatePlanetarySystem() = planetarySystemRandomGen.generate()

	private fun generateFlat() = flatRandomGen.generate()
}

sealed class ViewState {
	data class ShowPerson(val person: Person) : ViewState()
	data class ShowPlanetarySystem(val planetarySystem: PlanetarySystem) : ViewState()
	data class ShowFlat(val flat: Flat) : ViewState()
	object ShowNone : ViewState()
}