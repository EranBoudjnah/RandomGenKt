import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.jetbrains.kotlin.jvm")
}

dependencies {
	compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

ext {
	set("PUBLISH_GROUP_ID", "com.mitteloupe")
	set("PUBLISH_ARTIFACT_ID", "randomgenkt.datasource")
	set("PUBLISH_VERSION", "1.0.0")
}

apply {
	from("release-jar.gradle")
}

repositories {
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