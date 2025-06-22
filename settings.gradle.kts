rootProject.name = "pianoman"

include(":common")
include(":midi-server")

// Configure dependency resolution for all projects
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}