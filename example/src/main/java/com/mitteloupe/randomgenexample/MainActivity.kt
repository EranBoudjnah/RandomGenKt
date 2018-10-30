package com.mitteloupe.randomgenexample

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ViewFlipper
import com.mitteloupe.randomgenexample.data.flat.Flat
import com.mitteloupe.randomgenexample.data.person.Person
import com.mitteloupe.randomgenexample.data.planet.PlanetarySystem
import com.mitteloupe.randomgenexample.presentation.MainViewModel
import com.mitteloupe.randomgenexample.presentation.ViewState
import com.mitteloupe.randomgenexample.widget.FlatView
import com.mitteloupe.randomgenexample.widget.PersonView
import com.mitteloupe.randomgenexample.widget.PlanetarySystemView

class MainActivity : AppCompatActivity(), Observer<ViewState> {
	private lateinit var viewModel: MainViewModel

	private lateinit var personView: PersonView
	private lateinit var planetarySystemView: PlanetarySystemView
	private lateinit var flatView: FlatView

	private lateinit var viewFlipper: ViewFlipper

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		viewModel = MainViewModel()

		initViews()

		viewModel.viewState.observe(this, this)
	}

	override fun onChanged(viewState: ViewState?) {
		when (viewState) {
			is ViewState.ShowNone -> hideAllViews()
			is ViewState.ShowPerson -> showPersonView(viewState.person)
			is ViewState.ShowPlanetarySystem -> showPlanetarySystem(viewState.planetarySystem)
			is ViewState.ShowFlat -> showFlatView(viewState.flat)
		}
	}

	fun onPersonClick(view: View) {
		viewModel.onGeneratePersonClick()
	}

	fun onPlanetarySystemClick(view: View) {
		viewModel.onGeneratePlanetarySystemClick()
	}

	fun onFlatClick(view: View) {
		viewModel.onGenerateFlatClick()
	}

	private fun initViews() {
		viewFlipper = findViewById(R.id.content_container)
		personView = findViewById(R.id.person_view)
		planetarySystemView = findViewById(R.id.planetary_system_view)
		flatView = findViewById(R.id.flat_view)
	}

	private fun showPersonView(person: Person) {
		personView.setPerson(person)
		viewFlipper.displayedChild = Page.PERSON.pageNumber
	}

	private fun showPlanetarySystem(planetarySystem: PlanetarySystem) {
		planetarySystemView.setPlanetarySystem(planetarySystem)
		viewFlipper.displayedChild = Page.PLANETARY_SYSTEM.pageNumber
	}

	private fun showFlatView(flat: Flat) {
		flatView.setFlat(flat)
		viewFlipper.displayedChild = Page.FLAT.pageNumber
	}

	private fun hideAllViews() {
		viewFlipper.displayedChild = Page.NONE.pageNumber
	}

	private enum class Page constructor(val pageNumber: Int) {
		NONE(0),
		PERSON(1),
		PLANETARY_SYSTEM(2),
		FLAT(3)
	}
}
