import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.21"
    id("pl.allegro.tech.build.axion-release") version "1.12.1"
    id("java-library")
    id("maven")
}

group = "com.github.polesapart"
project.version = scmVersion.version

repositories {
    mavenCentral()
}

dependencies {
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    api("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    api ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    testImplementation(kotlin("test-junit"))
    testImplementation("com.squareup.retrofit2:retrofit:2.9.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    testImplementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    testImplementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
}

tasks.test {
    useJUnit()
    systemProperties["junit.jupiter.execution.parallel.enabled"] = true
    systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xjsr305=strict")
    }

    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks

compileTestKotlin.kotlinOptions {
    freeCompilerArgs += listOf("-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi")
}
