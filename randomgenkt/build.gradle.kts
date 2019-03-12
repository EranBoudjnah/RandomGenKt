import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	id("com.android.lint")
}

dependencies {
	compile(kotlin("stdlib-jdk8"))
	compile("org.jetbrains.kotlin:kotlin-reflect:1.3.21")

	implementation("com.implimentz:unsafe:0.0.6")

	testImplementation("junit:junit:4.12")
	testImplementation("org.hamcrest:hamcrest-core:1.3")
	testImplementation("org.mockito:mockito-core:2.23.0")
	testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
}

lintOptions {
	isAbortOnError = true
	isWarningsAsErrors = true
}

ext {
	set("PUBLISH_GROUP_ID", "com.mitteloupe")
	set("PUBLISH_ARTIFACT_ID", "randomgenkt")
	set("PUBLISH_VERSION", "1.0.0")
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
	jvmTarget = "1.6"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
	jvmTarget = "1.6"
}