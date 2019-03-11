import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.3.10"
}

buildscript {
	repositories {
		google()
		jcenter()
		mavenCentral()
	}
	dependencies {
		classpath("com.android.tools.build:gradle:3.3.2")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.10")
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
	compile(kotlin("stdlib-jdk8"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
	jvmTarget = "1.6"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
	jvmTarget = "1.6"
}
