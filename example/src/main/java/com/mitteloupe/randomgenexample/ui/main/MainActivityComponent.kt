package com.mitteloupe.randomgenexample.ui.main

import dagger.Subcomponent
import dagger.android.AndroidInjector


/**
 * Created by Eran Boudjnah on 31/10/2018.
 */
@Subcomponent(modules = [MainActivityModule::class])
interface MainActivityComponent : AndroidInjector<MainActivity> {
	@Subcomponent.Builder
	abstract class Builder : AndroidInjector.Builder<MainActivity>()
}