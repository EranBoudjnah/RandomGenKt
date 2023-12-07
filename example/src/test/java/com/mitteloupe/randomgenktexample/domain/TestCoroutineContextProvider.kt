package com.mitteloupe.randomgenktexample.domain

import kotlinx.coroutines.Dispatchers
import org.mockito.kotlin.given
import org.mockito.kotlin.mock

fun testCoroutineContextProvider(): CoroutineContextProvider {
    val coroutineContextProvider = mock<CoroutineContextProvider>()
    given(coroutineContextProvider.main).willReturn(Dispatchers.Unconfined)
    given(coroutineContextProvider.io).willReturn(Dispatchers.Unconfined)

    return coroutineContextProvider
}
