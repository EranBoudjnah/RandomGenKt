import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("com.android.application") version "8.3.0" apply false
    kotlin("android") version "1.9.22" apply false
    id("io.codearte.nexus-staging") version "0.30.0"
    id("com.google.dagger.hilt.android") version "2.51" apply false
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
