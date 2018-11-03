import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.3.0"
}

buildscript {
	repositories {
		google()
		jcenter()
	}
	dependencies {
		classpath("com.android.tools.build:gradle:3.2.1")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.0")
	}
}

allprojects {
	repositories {
		google()
		jcenter()
	}
}

repositories {
	mavenCentral()
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
