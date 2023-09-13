import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"

    kotlin("plugin.serialization") version "1.9.0"
    id("pl.allegro.tech.build.axion-release") version "1.15.1"
    id("java-library")
    `maven-publish`
}

group = "com.github.polesapart"
project.version = scmVersion.version

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")

    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test"))
    testImplementation("com.squareup.retrofit2:retrofit:2.9.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    testImplementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    testImplementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")
}

tasks.test {
    useJUnit()
    systemProperties["junit.jupiter.execution.parallel.enabled"] = true
    systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xjsr305=strict")
    }
}
tasks.withType<Test> {
    /**
     * fix for retrofit https://github.com/square/retrofit/issues/3341
     */
    jvmArgs = listOf("--add-opens", "java.base/java.lang.invoke=ALL-UNNAMED")
}

val compileTestKotlin: KotlinCompile by tasks

compileTestKotlin.kotlinOptions {
    freeCompilerArgs += listOf("-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi")
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }
}