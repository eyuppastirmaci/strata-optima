plugins {
    kotlin("jvm") version "2.2.20"

    application // Generates executable CLI tool
}

group = "com.eyuppastirmaci"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // Enables asynchronous, non-blocking parallel operations (Coroutines).
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // Coroutines testing support
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")

    // Handles command-line argument parsing and CLI interface creation.
    implementation("com.github.ajalt.clikt:clikt:4.2.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    // Sets the entry point of the application.
    mainClass.set("com.eyuppastirmaci.MainKt")
}