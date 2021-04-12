package com.mitteloupe.randomgenktexample.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.Job
import java.util.Random

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {
    @Provides
    @Reusable
    internal fun provideRandom() = Random()

    @Provides
    internal fun provideJob(): Job = Job()
}
