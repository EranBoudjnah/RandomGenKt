package com.mitteloupe.randomgenktexample.domain

import com.mitteloupe.randomgenktexample.data.generator.FlatGeneratorFactory
import com.mitteloupe.randomgenktexample.data.model.flat.Flat
import dagger.Reusable
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 30/10/2018.
 */
@Reusable
class GenerateFlatUseCase
@Inject constructor(
	coroutineContextProvider: CoroutineContextProvider,
	flatGeneratorFactory: FlatGeneratorFactory
) : BaseUseCase<Flat>(coroutineContextProvider) {
	private val flatRandomGen by lazy { flatGeneratorFactory.newFlatGenerator }

	override fun executeAsync() = flatRandomGen.generate()
}
