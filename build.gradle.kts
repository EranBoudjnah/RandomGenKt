import java.util.*
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("com.android.application") version "8.8.0" apply false
    kotlin("android") version "2.1.0" apply false
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("com.google.dagger.hilt.android") version "2.55" apply false
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

val properties = Properties()
val propertiesFile = project.rootProject.file("local.properties")
if (propertiesFile.exists()) {
    properties.load(propertiesFile.inputStream())
}
val ossrhUsername = properties["ossrhUsername"]
val ossrhPassword = properties["ossrhPassword"]

extra.set("PUBLISH_GROUP_ID", "com.mitteloupe.randomgenkt")

nexusPublishing {
    repositories {
        sonatype {
            username.set("$ossrhUsername")
            password.set("$ossrhPassword")
            packageGroup.set("${project.extra["PUBLISH_GROUP_ID"]}")
        }
    }
}
