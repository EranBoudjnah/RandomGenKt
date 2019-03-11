package com.mitteloupe.randomgenktexample.domain

import com.mitteloupe.randomgenktexample.data.generator.FlatGeneratorFactory
import com.mitteloupe.randomgenktexample.data.model.flat.Flat
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 30/10/2018.
 */
class GenerateFlatUseCase
@Inject constructor(
	private val coroutineContextProvider: CoroutineContextProvider,
	flatGeneratorFactory: FlatGeneratorFactory
) : BaseUseCase<Flat>(coroutineContextProvider) {
	private var flatRandomGen = flatGeneratorFactory.newFlatGenerator

	override suspend fun execute(callback: (Flat) -> Unit) {
		this.callback = callback

		uiScope.launch {
			var flat: Flat? = null

			withContext(coroutineContextProvider.io) {
				flat = executeAsync()
			}

			flat?.let(callback)
		}
	}

	private fun executeAsync() = flatRandomGen.generate()
}
