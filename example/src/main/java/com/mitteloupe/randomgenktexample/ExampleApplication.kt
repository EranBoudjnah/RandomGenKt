package com.mitteloupe.randomgenktexample

import android.app.Activity
import android.app.Application
import com.mitteloupe.randomgenktexample.di.AppComponent
import com.mitteloupe.randomgenktexample.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by Eran Boudjnah on 31/10/2018.
 */
class ExampleApplication : Application(), HasActivityInjector {
	@Inject
	internal lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

	private val appComponent: AppComponent by lazy {
		DaggerAppComponent
			.builder()
			.application(this)
			.build()
	}

	override fun onCreate() {
		super.onCreate()

		appComponent.inject(this)
	}

	override fun activityInjector() = activityDispatchingAndroidInjector
}
