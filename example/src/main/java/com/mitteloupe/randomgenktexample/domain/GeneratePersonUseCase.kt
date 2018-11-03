package com.mitteloupe.randomgenktexample.domain

import com.mitteloupe.randomgenktexample.data.generator.PersonGeneratorFactory
import com.mitteloupe.randomgenktexample.data.model.person.Person
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 30/10/2018.
 */
class GeneratePersonUseCase
@Inject
constructor(
	private val coroutineContextProvider: CoroutineContextProvider,
	personGeneratorFactory: PersonGeneratorFactory
) : BaseUseCase<Person>(coroutineContextProvider) {
	private var personRandomGen = personGeneratorFactory.newPersonGenerator

	override suspend fun execute(callback: (Person) -> Unit) {
		this.callback = callback

		uiScope.launch {
			var person: Person? = null

			withContext(coroutineContextProvider.io) {
				person = executeAsync()
			}

			person?.let(callback)
		}
	}

	fun executeAsync(): Person {
		return personRandomGen.generate()
	}
}
