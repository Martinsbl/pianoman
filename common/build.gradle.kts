plugins {
    kotlin("jvm")
    `java-library`
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