package com.mitteloupe.randomgenexample.di

import android.app.Activity
import com.mitteloupe.randomgenexample.ui.main.MainActivity
import com.mitteloupe.randomgenexample.ui.main.MainActivityComponent
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap


/**
 * Created by Eran Boudjnah on 31/10/2018.
 */
@Module
abstract class ActivityBuilder {
	@Binds
	@IntoMap
	@ActivityKey(MainActivity::class)
	internal abstract fun bindMainActivity(builder: MainActivityComponent.Builder): AndroidInjector.Factory<out Activity>
}