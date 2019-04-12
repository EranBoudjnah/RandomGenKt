package com.mitteloupe.randomgenktexample.di

import com.mitteloupe.randomgenktexample.ui.main.MainActivity
import com.mitteloupe.randomgenktexample.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Eran Boudjnah on 31/10/2018.
 */
@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    internal abstract fun mainActivity(): MainActivity
}