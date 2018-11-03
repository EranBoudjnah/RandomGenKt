package com.mitteloupe.randomgenktexample.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * Created by Eran Boudjnah on 30/10/2018.
 */
abstract class BaseUseCase<TYPE>(coroutineContextProvider: CoroutineContextProvider) {
	private val job = Job()
	protected val uiScope = CoroutineScope(coroutineContextProvider.main + job)

	protected lateinit var callback: (TYPE) -> Unit

	abstract suspend fun execute(callback: (TYPE) -> Unit)
}
