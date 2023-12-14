package com.mitteloupe.randomgenktexample.domain

import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify

class UseCaseExecutorTest {
    private lateinit var useCaseExecutor: UseCaseExecutor

    private lateinit var job: Job
    private lateinit var coroutineContextProvider: CoroutineContextProvider

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
