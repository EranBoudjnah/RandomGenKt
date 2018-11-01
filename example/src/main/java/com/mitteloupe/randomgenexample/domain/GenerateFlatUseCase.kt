package com.mitteloupe.randomgenexample.domain

import com.mitteloupe.randomgenexample.data.generator.FlatGeneratorFactory
import com.mitteloupe.randomgenexample.data.model.flat.Flat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 30/10/2018.
 */
class GenerateFlatUseCase
@Inject
constructor(
	flatGeneratorFactory: FlatGeneratorFactory
): BaseUseCase<Flat>() {
	private var flatRandomGen = flatGeneratorFactory.newFlatGenerator

	override suspend fun execute(callback: (Flat) -> Unit) {
		this.callback = callback

		uiScope.launch {
			var flat: Flat? = null

			withContext(Dispatchers.IO) {
				flat = executeAsync()
			}

			flat?.let(callback)
		}
	}

	fun executeAsync(): Flat {
		return flatRandomGen.generate()
	}
}
