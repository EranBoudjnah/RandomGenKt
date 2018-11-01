package com.mitteloupe.randomgenexample.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 31/10/2018.
 */
class UseCaseExecutor
@Inject constructor() {
	private val job = Job()
	private val uiScope = CoroutineScope(Dispatchers.Main + job)

	fun <TYPE> execute(useCase: BaseUseCase<TYPE>, callback: (TYPE) -> Unit) {
		uiScope.launch {
			useCase.execute(callback)
		}
	}

	fun abortAll() {
		job.cancel()
	}
}