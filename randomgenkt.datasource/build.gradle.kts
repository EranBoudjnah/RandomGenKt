plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

ext {
    set("PUBLISH_GROUP_ID", "com.mitteloupe.randomgenkt")
    set("PUBLISH_ARTIFACT_ID", "randomgenkt.datasource")
    set("PUBLISH_VERSION", "2.0.0")
}

apply {
    from("release-jar.gradle")
}
