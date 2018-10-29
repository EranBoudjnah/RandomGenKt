package com.mitteloupe.randomgen.fielddataprovider

import com.mitteloupe.randomgen.FieldDataProvider

import java.util.ArrayList
import java.util.Random

/**
 * A [FieldDataProvider] that routes to one of its [FieldDataProvider]s randomly with a weighted bias.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class WeightedFieldDataProvidersFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>
/**
 * Creates an instance of [WeightedFieldDataProvidersFieldDataProvider].
 *
 * @param fieldDataProvider An initial field data provider
 */
constructor(
	private val random: Random,
	private val fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>
) : FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE> {
	private val weightedFieldDataProviders: MutableList<WeightedFieldDataProvider>

	private val lastFieldDataProviderWeight: Double
		get() {
			if (weightedFieldDataProviders.isEmpty()) {
				return 0.0
			}

			val lastFieldDataProvider = weightedFieldDataProviders[weightedFieldDataProviders.size - 1]
			return lastFieldDataProvider.weight
		}

	init {
		weightedFieldDataProviders = ArrayList(1)

		addFieldDataProvider(fieldDataProvider, 1.0)
	}

	fun addFieldDataProvider(pFieldDataProvider: FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>, pWeight: Double) {
		val lastFieldDataProviderWeight = lastFieldDataProviderWeight

		val newFieldDataProviderWeight =
			if (lastFieldDataProviderWeight != 0.0)
				lastFieldDataProviderWeight * pWeight
			else
				pWeight

		val weightedFieldDataProvider = WeightedFieldDataProvider(
			pFieldDataProvider,
			newFieldDataProviderWeight,
			lastFieldDataProviderWeight + newFieldDataProviderWeight
		)
		weightedFieldDataProviders.add(weightedFieldDataProvider)
	}

	override fun generate(instance: OUTPUT_TYPE?): VALUE_TYPE {
		return generateRandomValue(instance)
	}

	private fun generateRandomValue(instance: OUTPUT_TYPE?): VALUE_TYPE {
		val randomWeightInRange = random.nextDouble() * lastFieldDataProviderWeight
		val position = getFieldDataProviderPositionByWeight(randomWeightInRange)

		return weightedFieldDataProviders[position]
			.fieldDataProvider
			.generate(instance)
	}

	private fun getFieldDataProviderPositionByWeight(pWeight: Double): Int {
		var lowIndex = 0
		var highIndex = weightedFieldDataProviders.size - 1

		while (highIndex >= lowIndex) {
			val currentIndex = (lowIndex + highIndex) / 2

			val weightedFieldDataProvider = weightedFieldDataProviders[currentIndex]
			val weightSumAtGuess = weightedFieldDataProvider.summedWeight
			val weightAtGuess = weightedFieldDataProvider.weight

			when {
				weightSumAtGuess < pWeight -> lowIndex = currentIndex + 1
				weightSumAtGuess - weightAtGuess > pWeight -> highIndex = currentIndex - 1
				else -> return currentIndex
			}
		}

		return 0
	}

	private inner class WeightedFieldDataProvider(
		internal var fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>,
		internal var weight: Double,
		internal var summedWeight: Double
	)
}
