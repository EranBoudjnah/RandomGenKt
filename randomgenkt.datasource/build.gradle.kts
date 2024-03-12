import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

ext {
    set("PUBLISH_GROUP_ID", "com.mitteloupe.randomgenkt")
    set("PUBLISH_ARTIFACT_ID", "randomgenkt.datasource")
    set("PUBLISH_VERSION", "2.0.1")
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
