package com.mitteloupe.randomgenexample

import android.app.Activity
import android.app.Application
import com.mitteloupe.randomgenexample.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 31/10/2018.
 */
class ExampleApplication : Application(), HasActivityInjector {
	@Inject
	internal lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

	override fun onCreate() {
		super.onCreate()

		DaggerAppComponent
			.builder()
			.application(this)
			.build()
			.inject(this)
	}

	override fun activityInjector(): DispatchingAndroidInjector<Activity> {
		return activityDispatchingAndroidInjector
	}
}
