package com.mitteloupe.randomgenktexample.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Created by Eran Boudjnah on 01/11/2018.
 */
class UseCaseExecutorTest {
    lateinit var useCaseExecutor: UseCaseExecutor

    lateinit var job: Job
    lateinit var coroutineContextProvider: CoroutineContextProvider

    @Before
    fun setUp() {
        job = spy(Job())
        coroutineContextProvider = testCoroutineContextProvider()
        useCaseExecutor = UseCaseExecutor(job, coroutineContextProvider)
    }

    @Test
    fun `Given use case and callback when execute then executes use case with callback`() {
        // Given
        val useCase = mock<BaseUseCase<Any>>()
        val callback: (Any) -> Unit = {}

        // When
        useCaseExecutor.execute(useCase, callback)

        // Then
        runBlocking {
            verify(useCase).execute(callback)
        }
    }

    @Test
    fun `Given job when abortAll then cancels provided job`() {
        // When
        useCaseExecutor.abortAll()

        // Then
        verify(job).cancel()
    }
}