package com.mitteloupe.randomgenktexample.domain

import dagger.Reusable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 31/10/2018.
 */
@Reusable
class UseCaseExecutor
@Inject constructor(
    private val job: Job,
    coroutineContextProvider: CoroutineContextProvider
) {
    private val uiScope by lazy {
        CoroutineScope(coroutineContextProvider.main + job)
    }

    fun <TYPE> execute(useCase: BaseUseCase<TYPE>, callback: (TYPE) -> Unit) =
        uiScope.launch {
            useCase.execute(callback)
        }

    fun abortAll() = job.cancel()
}