package com.mitteloupe.randomgenktexample.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AppModule {
    @Provides
    internal fun provideContext(application: Application): Context = application
}
