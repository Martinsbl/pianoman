val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.2.0"
    id("com.google.cloud.tools.jib") version "3.4.5"
}

group = "net.testiprod"
version = "0.0.1"

application {
    mainClass = "net.testiprod.pianoman.ApplicationKt"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-swagger")
    implementation("io.ktor:ktor-server-sse")
    implementation("io.ktor:ktor-server-host-common")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-gson")
    implementation("io.ktor:ktor-server-di")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

jib {
    from {
        image = "registry.access.redhat.com/ubi8/openjdk-21-runtime"
    }
    to {
        image = "testiprod/pianoman"
        tags = setOf("latest", "$version")
    }
    container {
        mainClass = "net.testiprod.pianoman.ApplicationKt"

        ports = listOf("8080")

        creationTime = "USE_CURRENT_TIMESTAMP"
    }
}