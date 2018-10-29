package com.mitteloupe.randomgenexample

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ViewFlipper

import com.mitteloupe.randomgen.RandomGen
import com.mitteloupe.randomgenexample.data.flat.Flat
import com.mitteloupe.randomgenexample.data.person.Person
import com.mitteloupe.randomgenexample.data.planet.PlanetarySystem
import com.mitteloupe.randomgenexample.generator.FlatGeneratorFactory
import com.mitteloupe.randomgenexample.generator.PersonGeneratorFactory
import com.mitteloupe.randomgenexample.generator.PlanetarySystemGeneratorFactory
import com.mitteloupe.randomgenexample.widget.FlatView
import com.mitteloupe.randomgenexample.widget.PersonView
import com.mitteloupe.randomgenexample.widget.PlanetarySystemView

import java.util.Random

class MainActivity : AppCompatActivity() {
	private lateinit var handler: Handler
	private lateinit var flatGeneratorFactory: FlatGeneratorFactory
	private lateinit var personGeneratorFactory: PersonGeneratorFactory
	private lateinit var planetarySystemGeneratorFactory: PlanetarySystemGeneratorFactory

	private lateinit var personRandomGen: RandomGen<Person>
	private lateinit var personView: PersonView

	private lateinit var planetarySystemRandomGen: RandomGen<PlanetarySystem>
	private lateinit var planetarySystemView: PlanetarySystemView

	private lateinit var flatRandomGen: RandomGen<Flat>
	private lateinit var flatView: FlatView

	private lateinit var viewFlipper: ViewFlipper

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		handler = Handler()
		initViews()

		handler.post { initGenerators() }
	}

	private fun initViews() {
		viewFlipper = findViewById(R.id.content_container)
		personView = findViewById(R.id.person_view)
		planetarySystemView = findViewById(R.id.planetary_system_view)
		flatView = findViewById(R.id.flat_view)
	}

	private fun initGenerators() {
		flatGeneratorFactory = FlatGeneratorFactory(Random())
		personGeneratorFactory = PersonGeneratorFactory()
		planetarySystemGeneratorFactory = PlanetarySystemGeneratorFactory()
		personRandomGen = personGeneratorFactory.newPersonGenerator
		planetarySystemRandomGen = planetarySystemGeneratorFactory.newPlanetarySystemGenerator
		flatRandomGen = flatGeneratorFactory.newFlatGenerator
	}

	fun onPersonClick(view: View) {
		handler.post { generatePerson() }
	}

	fun onPlanetarySystemClick(view: View) {
		handler.post { generatePlanetarySystem() }
	}

	fun onFlatClick(view: View) {
		handler.post { generateFlat() }
	}

	private fun generatePerson() {
		val person = personRandomGen.generate()
		viewFlipper.displayedChild = 1
		personView.setPerson(person)
	}

	private fun generatePlanetarySystem() {
		val planetarySystem = planetarySystemRandomGen.generate()
		viewFlipper.displayedChild = 2
		planetarySystemView.setPlanetarySystem(planetarySystem)
	}

	private fun generateFlat() {
		val flat = flatRandomGen.generate()
		viewFlipper.displayedChild = 3
		flatView.setFlat(flat)
	}
}
