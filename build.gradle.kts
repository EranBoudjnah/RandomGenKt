import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
    id("com.github.ben-manes.versions") version "0.38.0"
    id("io.codearte.nexus-staging") version "0.30.0"
}

buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath(kotlin("gradle-plugin", version = "1.3.61"))
        classpath("com.github.ben-manes:gradle-versions-plugin:0.38.0")
        classpath("io.codearte.gradle.nexus:gradle-nexus-staging-plugin:0.30.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.34-beta")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.6"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.6"
}

val ktlintVerify: JavaExec by tasks.creating(JavaExec::class)

val ktlintFormat: JavaExec by tasks.creating(JavaExec::class)


