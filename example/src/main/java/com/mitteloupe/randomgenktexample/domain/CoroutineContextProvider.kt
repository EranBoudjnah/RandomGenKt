package com.mitteloupe.randomgenktexample.domain

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Eran Boudjnah on 01/11/2018.
 */
class CoroutineContextProvider
@Inject
constructor() {
	val main: CoroutineContext by lazy { Dispatchers.Main }
	val io: CoroutineContext by lazy { Dispatchers.IO }
}