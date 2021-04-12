plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
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

repositories {
    google()
    jcenter()
    mavenCentral()
    maven("https://dl.bintray.com/shadowcra/RandomGenKt")
}

val ktlint: Configuration by configurations.creating

dependencies {
    ktlint("com.github.shyiko:ktlint:0.31.0")

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.2")

    implementation("com.google.dagger:dagger:2.19")
    annotationProcessor("com.google.dagger:dagger-compiler:2.19")
    kapt("com.google.dagger:dagger-compiler:2.19")

    implementation("com.google.dagger:dagger-android:2.18")
    implementation("com.google.dagger:dagger-android-support:2.18")
    annotationProcessor("com.google.dagger:dagger-android-processor:2.18")
    kapt("com.google.dagger:dagger-android-processor:2.18")

    testImplementation("junit:junit:4.12")
    testImplementation("org.hamcrest:hamcrest-core:2.2")
    testImplementation("org.mockito:mockito:3.8.0")
    testImplementation("org.mockito:mockito-inline:3.8.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testImplementation("android.arch.core:core-testing:1.1.1")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.3.0")
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test:rules:1.3.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")

    implementation("com.mitteloupe:randomgenkt:1.0.1")
}

val ktlintVerify: JavaExec by tasks.creating(JavaExec::class)
ktlintVerify.apply {
    description = "Check Kotlin code style."
    main = "com.github.shyiko.ktlint.Main"
    classpath = ktlint
    args("**/*.gradle.kts", "**/*.kt")
}
tasks["check"].dependsOn(ktlintVerify)

val ktlintFormat: JavaExec by tasks.creating(JavaExec::class)
ktlintFormat.apply {
    description = "Fix Kotlin code style deviations."
    main = ktlintVerify.main
    classpath = ktlintVerify.classpath
    args("-F")
    args(ktlintVerify.args)
}
