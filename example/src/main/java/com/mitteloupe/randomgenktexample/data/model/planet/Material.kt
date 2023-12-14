package com.mitteloupe.randomgenktexample.data.model.planet

class Material @SafeVarargs constructor(vararg compounds: Pair<String, Int>) {
    val compounds: List<Pair<String, Int>> by lazy {
        compounds.toMutableList()
    }
}
