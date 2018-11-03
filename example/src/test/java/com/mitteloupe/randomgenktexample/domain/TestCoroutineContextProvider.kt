package com.mitteloupe.randomgenktexample.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers

/**
 * Created by Eran Boudjnah on 01/11/2018.
 */
fun testCoroutineContextProvider(): CoroutineContextProvider {
	val coroutineContextProvider = mock<CoroutineContextProvider>()
	whenever(coroutineContextProvider.main).thenReturn(Dispatchers.Unconfined)
	whenever(coroutineContextProvider.io).thenReturn(Dispatchers.Unconfined)

	return coroutineContextProvider
}
