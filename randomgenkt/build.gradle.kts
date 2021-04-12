import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.android.lint")
}

val ktlint: Configuration by configurations.creating

dependencies {
    ktlint("com.github.shyiko:ktlint:0.31.0")

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")

    implementation("com.implimentz:unsafe:0.0.6")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest-core:2.2")
    testImplementation("org.mockito:mockito-core:3.9.0")
    testImplementation("org.mockito:mockito-inline:3.9.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}

lintOptions {
    isAbortOnError = true
    isWarningsAsErrors = true
}

ext {
    set("PUBLISH_GROUP_ID", "com.mitteloupe")
    set("PUBLISH_ARTIFACT_ID", "randomgenkt")
    set("PUBLISH_VERSION", "1.0.1")
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

val ktlintVerify: JavaExec by tasks.creating(JavaExec::class)
ktlintVerify.apply {
    description = "Check Kotlin code style."
    main = "com.github.shyiko.ktlint.Main"
    classpath = ktlint
    args("**/*.gradle.kts", "**/*.kt")
}
tasks["check"].dependsOn(ktlintVerify)

val ktlintFormat: JavaExec by tasks.creating(JavaExec::class)
ktlintFormat.apply {
    description = "Fix Kotlin code style deviations."
    main = ktlintVerify.main
    classpath = ktlintVerify.classpath
    args("-F")
    args(ktlintVerify.args)
}
