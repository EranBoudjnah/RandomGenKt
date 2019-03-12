package com.mitteloupe.randomgenktexample.domain

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers

/**
 * Created by Eran Boudjnah on 01/11/2018.
 */
fun testCoroutineContextProvider(): CoroutineContextProvider {
	val coroutineContextProvider = mock<CoroutineContextProvider>()
	given(coroutineContextProvider.main).willReturn(Dispatchers.Unconfined)
	given(coroutineContextProvider.io).willReturn(Dispatchers.Unconfined)

	return coroutineContextProvider
}
