package com.mitteloupe.randomgenktexample.ui.main

import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.coroutines.Job
import java.util.Random

@Module
class MainActivityModule {
	@Provides
	@Reusable
	internal fun provideRandom(): Random {
		return Random()
	}

	@Provides
	internal fun provideJob(): Job {
		return Job()
	}
}