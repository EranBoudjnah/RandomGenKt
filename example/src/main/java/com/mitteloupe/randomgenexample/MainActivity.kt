package com.mitteloupe.randomgenexample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.mitteloupe.randomgenexample.data.model.flat.Flat
import com.mitteloupe.randomgenexample.data.model.person.Person
import com.mitteloupe.randomgenexample.data.model.planet.PlanetarySystem
import com.mitteloupe.randomgenexample.presentation.MainViewModel
import com.mitteloupe.randomgenexample.presentation.ViewState
import kotlinx.android.synthetic.main.activity_main.content_container as viewFlipper
import kotlinx.android.synthetic.main.activity_main.flat_view as flatView
import kotlinx.android.synthetic.main.activity_main.person_view as personView
import kotlinx.android.synthetic.main.activity_main.planetary_system_view as planetarySystemView

class MainActivity : AppCompatActivity(), Observer<ViewState> {
	private lateinit var viewModel: MainViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		viewModel = MainViewModel()

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

	@Suppress("UNUSED_PARAMETER")
	fun onPersonClick(view: View) {
		viewModel.onGeneratePersonClick()
	}

	@Suppress("UNUSED_PARAMETER")
	fun onPlanetarySystemClick(view: View) {
		viewModel.onGeneratePlanetarySystemClick()
	}

	@Suppress("UNUSED_PARAMETER")
	fun onFlatClick(view: View) {
		viewModel.onGenerateFlatClick()
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
