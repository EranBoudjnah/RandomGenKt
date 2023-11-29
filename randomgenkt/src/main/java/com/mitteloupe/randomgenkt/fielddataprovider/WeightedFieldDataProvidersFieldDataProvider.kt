package com.mitteloupe.randomgenkt.fielddataprovider

import com.mitteloupe.randomgenkt.FieldDataProvider
import java.util.Random

/**
 * A lambda that routes to one of its lambdas randomly with a weighted bias.
 *
 * Created by Eran Boudjnah on 24/04/2018.
 */
class WeightedFieldDataProvidersFieldDataProvider<OUTPUT_TYPE, VALUE_TYPE : Any>(
    private val random: Random,
    private val fieldDataProvider: FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>
) : FieldDataProvider<OUTPUT_TYPE, VALUE_TYPE>() {
    private val weightedFieldDataProviders: MutableList<WeightedFieldDataProvider> by lazy {
        val weightedFieldDataProvider = getWeightedFieldDataProvider(0.0, fieldDataProvider, 1.0)
        arrayListOf(weightedFieldDataProvider)
    }

    private val lastFieldDataProviderWeight: Double
        get() =
            when {
                weightedFieldDataProviders.isEmpty() -> 0.0
                else -> weightedFieldDataProviders.last().weight
            }

    fun addFieldDataProvider(fieldDataProvider: (OUTPUT_TYPE?) -> VALUE_TYPE, weight: Double) {
        val weightedFieldDataProvider =
            getWeightedFieldDataProvider(lastFieldDataProviderWeight, fieldDataProvider, weight)
        weightedFieldDataProviders.add(weightedFieldDataProvider)
    }

    private fun getWeightedFieldDataProvider(
        lastWeight: Double,
        fieldDataProvider: (OUTPUT_TYPE?) -> VALUE_TYPE,
        weight: Double
    ): WeightedFieldDataProvider {
        val newFieldDataProviderWeight = if (lastWeight == 0.0) {
            weight
        } else {
            lastWeight * weight
        }

        return WeightedFieldDataProvider(
            fieldDataProvider,
            newFieldDataProviderWeight,
            lastWeight + newFieldDataProviderWeight
        )
    }

    override fun invoke(instance: OUTPUT_TYPE?) = generateRandomValue(instance)

    private fun generateRandomValue(instance: OUTPUT_TYPE?): VALUE_TYPE {
        val randomWeightInRange = random.nextDouble() * lastFieldDataProviderWeight
        val position = getFieldDataProviderPositionByWeight(randomWeightInRange)

        return weightedFieldDataProviders[position]
            .fieldDataProvider(instance)
    }

    private fun getFieldDataProviderPositionByWeight(weight: Double): Int {
        var lowIndex = 0
        var highIndex = weightedFieldDataProviders.size - 1

        while (highIndex >= lowIndex) {
            val currentIndex = (lowIndex + highIndex) / 2

            val weightedFieldDataProvider = weightedFieldDataProviders[currentIndex]
            val weightSumAtGuess = weightedFieldDataProvider.summedWeight
            val weightAtGuess = weightedFieldDataProvider.weight

            when {
                weightSumAtGuess < weight -> lowIndex = currentIndex + 1
                weightSumAtGuess - weightAtGuess > weight -> highIndex = currentIndex - 1
                else -> return currentIndex
            }
        }

        return 0
    }

    private inner class WeightedFieldDataProvider(
        val fieldDataProvider: (OUTPUT_TYPE?) -> VALUE_TYPE,
        val weight: Double,
        val summedWeight: Double
    )
}
