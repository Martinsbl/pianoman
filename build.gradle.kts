val kotlin_version: String by project
val logback_version: String by project

apply(from = "openapi.gradle.kts")

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
    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("io.ktor:ktor-serialization-gson")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-di")
    implementation("io.ktor:ktor-server-host-common")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-sse")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-server-swagger")
    implementation("io.ktor:ktor-server-websockets")

    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

jib {
    from {
        image = "eclipse-temurin:21-jre-alpine"
    }
    to {
        image = "ghcr.io/martinsbl/pianoman"
        tags = setOf("latest", "$version")
        auth {
            username = "martinsbl"
            password = findProperty("github.token") as String?
                ?: System.getenv("GH_ACTIONS_TOKEN")
                        ?: "'github.token' is not set. Missing variable in gradle.properties?".also { println("WARNING: $it") }
        }
    }
    container {
        mainClass = "net.testiprod.pianoman.ApplicationKt"

        ports = listOf("8080")

        creationTime = "USE_CURRENT_TIMESTAMP"
    }
}
