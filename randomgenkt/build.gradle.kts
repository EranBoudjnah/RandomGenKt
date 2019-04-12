import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.android.lint")
}

val ktlint: Configuration by configurations.creating

dependencies {
    ktlint("com.github.shyiko:ktlint:0.31.0")

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
