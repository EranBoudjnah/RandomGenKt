package com.mitteloupe.randomgenexample.ui.main

import dagger.Module
import dagger.Provides
import dagger.Reusable
import java.util.Random

@Module
class MainActivityModule {
	@Provides
	@Reusable
	internal fun provideRandom(): Random {
		return Random()
	}
}