plugins {
    kotlin("jvm") version "2.1.10" apply false  // Use latest Kotlin version
    // Add other common plugins here that modules might need
}

// Common configuration for all projects
allprojects {
    group = "net.testiprod"
    version = "0.0.1"
}

// Configuration for all subprojects
subprojects {
    repositories {
        mavenCentral()
    }
}