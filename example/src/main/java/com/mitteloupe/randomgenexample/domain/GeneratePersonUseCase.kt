package com.mitteloupe.randomgenexample.domain

import com.mitteloupe.randomgenexample.data.generator.PersonGeneratorFactory
import com.mitteloupe.randomgenexample.data.model.person.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 30/10/2018.
 */
class GeneratePersonUseCase
@Inject
constructor(
	personGeneratorFactory: PersonGeneratorFactory
) : BaseUseCase<Person>() {
	private var personRandomGen = personGeneratorFactory.newPersonGenerator

	override suspend fun execute(callback: (Person) -> Unit) {
		this.callback = callback

		uiScope.launch {
			var person: Person? = null

			withContext(Dispatchers.IO) {
				person = executeAsync()
			}

			person?.let(callback)
		}
	}

	fun executeAsync(): Person {
		return personRandomGen.generate()
	}
}
