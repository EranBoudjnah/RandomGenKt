import java.util.*
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.gradle.nexus.publish)
    alias(libs.plugins.hilt.android) apply false
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
