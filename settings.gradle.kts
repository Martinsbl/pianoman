rootProject.name = "pianoman"

include(":common")
include(":midi-server")
include(":midi-client")

// Configure dependency resolution for all projects
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
