plugins {
    kotlin("jvm")
    id("com.android.lint")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.20")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest-core:2.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
}

ext {
    set("PUBLISH_GROUP_ID", "com.mitteloupe.randomgenkt")
    set("PUBLISH_ARTIFACT_ID", "randomgenkt")
    set("PUBLISH_VERSION", "2.0.0")
}

apply {
    from("release-jar.gradle")
}
