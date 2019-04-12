package com.mitteloupe.randomgenktexample.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Eran Boudjnah on 30/10/2018.
 */
abstract class BaseUseCase<TYPE>(
    private val coroutineContextProvider: CoroutineContextProvider
) {
    private val job by lazy { Job() }
    private val uiScope by lazy {
        CoroutineScope(coroutineContextProvider.main + job)
    }

    private lateinit var callback: (TYPE) -> Unit

    suspend fun execute(callback: (TYPE) -> Unit) {
        this.callback = callback

        uiScope.launch {
            val result = withContext(coroutineContextProvider.io) {
                executeAsync()
            }

            callback(result)
        }
    }

    abstract fun executeAsync(): TYPE
}
