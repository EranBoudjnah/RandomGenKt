package com.mitteloupe.randomgenexample.di

import com.mitteloupe.randomgenexample.ui.main.MainActivity
import com.mitteloupe.randomgenexample.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Eran Boudjnah on 31/10/2018.
 */
@Module
abstract class ActivityBuilder {
	@ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class))
	internal abstract fun mainActivity(): MainActivity
}