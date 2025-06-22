plugins {
    kotlin("jvm")
//    `java-library`  // Adds library-specific features
}

group = "net.testiprod.pianoman"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}