package com.mitteloupe.randomgenktexample.domain

import dagger.Reusable
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

/**
 * Created by Eran Boudjnah on 01/11/2018.
 */
@Reusable
class CoroutineContextProvider
@Inject
constructor() {
    val main: CoroutineContext by lazy { Dispatchers.Main }
    val io: CoroutineContext by lazy { Dispatchers.IO }
}
