val logback_version: String by project

plugins {
    kotlin("jvm")
    id("io.ktor.plugin") version "3.2.0"
    application // TODO `java-library`?
}

group = "net.testiprod"
version = "0.0.1"

application {
    mainClass = "net.testiprod.midi.client.ClientApplicationkt"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:${logback_version}")

    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-serialization-gson")
    implementation(project(":common"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}