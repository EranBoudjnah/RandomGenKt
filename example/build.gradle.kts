plugins {
	id("com.android.application")
	id("kotlin-android")
	id("kotlin-android-extensions")
	id("kotlin-kapt")
}

android {
	compileSdkVersion(28)

	defaultConfig {
		applicationId = "com.mitteloupe.randomgenexample"
		minSdkVersion(24)
		targetSdkVersion(28)
		versionCode = 1
		versionName = "1.0"
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}
	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
		}
	}
}

dependencies {
	implementation("androidx.appcompat:appcompat:1.0.0")
	implementation("androidx.constraintlayout:constraintlayout:1.1.3")
	implementation("com.google.android.material:material:1.0.0")
	implementation("androidx.lifecycle:lifecycle-extensions:2.0.0")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.0.0")

	implementation("com.google.dagger:dagger:2.19")
	annotationProcessor("com.google.dagger:dagger-compiler:2.19")
	kapt("com.google.dagger:dagger-compiler:2.19")

	implementation("com.google.dagger:dagger-android:2.15")
	implementation("com.google.dagger:dagger-android-support:2.15")
	annotationProcessor("com.google.dagger:dagger-android-processor:2.16")
	kapt("com.google.dagger:dagger-android-processor:2.16")

	testImplementation("junit:junit:4.12")
	testImplementation("org.hamcrest:hamcrest-core:1.3")
	testImplementation("org.mockito:mockito-core:2.23.0")
	testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC3")
	testImplementation("android.arch.core:core-testing:1.1.1")

	androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0")
	androidTestImplementation("androidx.test.espresso:espresso-contrib:3.1.0")
	androidTestImplementation("androidx.test:runner:1.1.0")
	androidTestImplementation("androidx.test:rules:1.1.0")
	androidTestImplementation("androidx.test.ext:junit:1.0.0")

//	implementation 'com.mitteloupe:randomgen:1.4.0'
	implementation(project(":randomgenkt"))
}

