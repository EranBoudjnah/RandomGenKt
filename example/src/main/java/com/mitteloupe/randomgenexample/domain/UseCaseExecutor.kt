package com.mitteloupe.randomgenexample.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 31/10/2018.
 */
class UseCaseExecutor
@Inject constructor(
	private val job: Job,
	coroutineContextProvider: CoroutineContextProvider
) {
	private val uiScope = CoroutineScope(coroutineContextProvider.main + job)

	fun <TYPE> execute(useCase: BaseUseCase<TYPE>, callback: (TYPE) -> Unit) {
		uiScope.launch {
			useCase.execute(callback)
		}
	}

	fun abortAll() {
		job.cancel()
	}
}