import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.3.21"
}

buildscript {
	repositories {
		google()
		jcenter()
		mavenCentral()
	}
	dependencies {
		classpath("com.android.tools.build:gradle:3.5.3")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
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

val ktlintVerify: JavaExec by tasks.creating(JavaExec::class)

val ktlintFormat: JavaExec by tasks.creating(JavaExec::class)


