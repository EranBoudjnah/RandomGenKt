package com.mitteloupe.randomgenexample.data.planet

import android.support.v4.util.Pair

/**
 * Created by Eran Boudjnah on 19/08/2018.
 */
class Material @SafeVarargs
constructor(vararg compound: Pair<String, Int>) {
	val compound: List<Pair<String, Int>> by lazy { compound.toMutableList() }
}
