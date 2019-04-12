package com.mitteloupe.randomgenktexample.di

import android.app.Application
import com.mitteloupe.randomgenktexample.ExampleApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule

/**
 * Created by Eran Boudjnah on 31/10/2018.
 */
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityBuilder::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: ExampleApplication)
}