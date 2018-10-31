import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.jetbrains.kotlin.jvm")
	id("com.android.lint")
}

dependencies {
	compile(kotlin("stdlib-jdk8"))
	compile("org.jetbrains.kotlin:kotlin-reflect:1.3.0")

	implementation("com.implimentz:unsafe:0.0.6")

	testImplementation("junit:junit:4.12")
	testImplementation("org.hamcrest:hamcrest-core:1.3")
	testImplementation("org.mockito:mockito-core:2.23.0")
	testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC3")
}

lintOptions {
	isAbortOnError = true
	isWarningsAsErrors = true
}

ext {
	set("PUBLISH_GROUP_ID", "com.mitteloupe")
	set("PUBLISH_ARTIFACT_ID", "randomgen")
	set("PUBLISH_VERSION", "1.4.0")
}

apply {
	from("release-jar.gradle")
}

repositories {
	jcenter()
	mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
	jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
	jvmTarget = "1.8"
}