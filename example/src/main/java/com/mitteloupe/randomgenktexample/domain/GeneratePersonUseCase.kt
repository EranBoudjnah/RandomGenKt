package com.mitteloupe.randomgenktexample.domain

import com.mitteloupe.randomgenktexample.data.generator.PersonGeneratorFactory
import com.mitteloupe.randomgenktexample.data.model.person.Person
import dagger.Reusable
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 30/10/2018.
 */
@Reusable
class GeneratePersonUseCase
@Inject constructor(
    coroutineContextProvider: CoroutineContextProvider,
    personGeneratorFactory: PersonGeneratorFactory
) : BaseUseCase<Person>(coroutineContextProvider) {
    private val personRandomGen by lazy { personGeneratorFactory.newPersonGenerator }

    override fun executeAsync() = personRandomGen.generate()
}
