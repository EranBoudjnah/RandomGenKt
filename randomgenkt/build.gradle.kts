import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm")
    id("com.android.lint")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.21")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest-core:2.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
}

ext {
    set("PUBLISH_GROUP_ID", "com.mitteloupe.randomgenkt")
    set("PUBLISH_ARTIFACT_ID", "randomgenkt")
    set("PUBLISH_VERSION", "2.0.0")
}

apply {
    from("release-jar.gradle")
}

tasks.withType(Test::class) {
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = setOf(
            TestLogEvent.SKIPPED,
            TestLogEvent.PASSED,
            TestLogEvent.FAILED
        )
        showStandardStreams = true
    }
}
