package com.mitteloupe.randomgenexample.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * Created by Eran Boudjnah on 30/10/2018.
 */
abstract class BaseUseCase<TYPE> {
	private val job = Job()
	protected val uiScope = CoroutineScope(Dispatchers.Main + job)

	protected lateinit var callback: (TYPE) -> Unit

	abstract suspend fun execute(callback: (TYPE) -> Unit)
}
