package com.mitteloupe.randomgenexample.di

import android.app.Application
import android.content.Context
import com.mitteloupe.randomgenexample.ui.main.MainActivityComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Eran Boudjnah on 31/10/2018.
 */
@Module(subcomponents = [
	MainActivityComponent::class
])
class AppModule {

	@Provides
	@Singleton
	internal fun provideContext(application: Application): Context {
		return application
	}
}