package com.mitteloupe.randomgenktexample.ui.main

import android.os.Bundle
import android.view.MenuItem
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mitteloupe.randomgenktexample.R
import com.mitteloupe.randomgenktexample.data.model.flat.Flat
import com.mitteloupe.randomgenktexample.data.model.person.Person
import com.mitteloupe.randomgenktexample.data.model.planet.PlanetarySystem
import com.mitteloupe.randomgenktexample.presentation.MainViewModel
import com.mitteloupe.randomgenktexample.presentation.MainViewModelFactory
import com.mitteloupe.randomgenktexample.presentation.ViewState
import com.mitteloupe.randomgenktexample.widget.FlatView
import com.mitteloupe.randomgenktexample.widget.PlanetarySystemView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Observer<ViewState> {
    private val viewFlipper: ViewFlipper by lazy { findViewById(R.id.content_container) }
    private val peopleView: RecyclerView by lazy { findViewById(R.id.people_view) }
    private val flatView: FlatView by lazy { findViewById(R.id.flat_view) }
    private val planetarySystemView: PlanetarySystemView by lazy {
        findViewById(R.id.planetary_system_view)
    }
    private val bottomNavigation: BottomNavigationView by lazy {
        findViewById(R.id.bottom_navigation)
    }

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private lateinit var viewModel: MainViewModel

    private val peopleAdapter = PeopleAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpNavigation()

        setUpPeopleView()

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.viewState.observe(this, this)
    }

    private fun setUpPeopleView() {
        peopleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        peopleView.adapter = peopleAdapter
        peopleView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun setUpNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            handleNavigationItemSelected(item)

            true
        }
    }

    private fun handleNavigationItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_person -> viewModel.onGeneratePersonClick()
        R.id.action_planetary_system -> viewModel.onGeneratePlanetarySystemClick()
        R.id.action_flat -> viewModel.onGenerateFlatClick()
        else -> null
    }

    override fun onChanged(viewState: ViewState) = updateViewWithViewState(viewState)

    private fun updateViewWithViewState(viewState: ViewState) {
        when (viewState) {
            is ViewState.ShowNone -> hideAllViews()
            is ViewState.ShowPeople -> showPeopleView(viewState.people)
            is ViewState.ShowPlanetarySystem -> showPlanetarySystem(viewState.planetarySystem)
            is ViewState.ShowFlat -> showFlatView(viewState.flat)
        }
    }

    private fun showPeopleView(people: List<Person>) {
        peopleAdapter.setPeople(people)
        viewFlipper.displayedChild = Page.PERSON.pageNumber
        bottomNavigation.selectedItemId = Page.PERSON.pageNumber
        peopleView.scrollToPosition(0)
    }

    private fun showPlanetarySystem(planetarySystem: PlanetarySystem) {
        planetarySystemView.setPlanetarySystem(planetarySystem)
        viewFlipper.displayedChild = Page.PLANETARY_SYSTEM.pageNumber
        bottomNavigation.selectedItemId = Page.PLANETARY_SYSTEM.pageNumber
    }

    private fun showFlatView(flat: Flat) {
        flatView.setFlat(flat)
        viewFlipper.displayedChild = Page.FLAT.pageNumber
        bottomNavigation.selectedItemId = Page.FLAT.pageNumber
    }

    private fun hideAllViews() {
        viewFlipper.displayedChild = Page.NONE.pageNumber
        bottomNavigation.selectedItemId = R.id.action_none
    }

    private enum class Page constructor(val pageNumber: Int) {
        NONE(0),
        PERSON(1),
        PLANETARY_SYSTEM(2),
        FLAT(3)
    }
}
