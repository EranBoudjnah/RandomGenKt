plugins {
	id("com.android.application")
	id("kotlin-android")
	id("kotlin-android-extensions")
	id("kotlin-kapt")
}

android {
	compileSdkVersion(27)

	defaultConfig {
		applicationId = "com.mitteloupe.randomgenexample"
		minSdkVersion(24)
		targetSdkVersion(27)
		versionCode = 1
		versionName = "1.0"
		testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
	}
	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
		}
	}
}

dependencies {
	implementation("com.android.support:appcompat-v7:27.1.1")
	implementation("com.android.support.constraint:constraint-layout:1.1.3")
	implementation("android.arch.lifecycle:extensions:1.1.1")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.0.0")

	testImplementation("junit:junit:4.12")
	testImplementation("org.hamcrest:hamcrest-core:1.3")

	androidTestImplementation("com.android.support.test:runner:1.0.2")

//	implementation 'com.mitteloupe:randomgen:1.4.0'
	implementation(project(":randomgen"))
}

