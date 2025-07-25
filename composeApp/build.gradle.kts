import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.jetbrains.androidx.lifecycle.viemodel)
            implementation(libs.langchain4j.openai)
            implementation(libs.langchain4j)
            implementation(project(":midi-client"))
            implementation(project(":common")) // TODO: Use `java-library` and api() to get transitive dependencies
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.logback)
            implementation("com.typesafe:config:1.4.3")
        }
    }
}

// needs libs.plugins.androidApplication plugin
//dependencies {
//    debugImplementation(compose.uiTooling)
//}

compose.desktop {
    application {
        mainClass = "net.testiprod.pianoman.app.desktop.Desktop.kt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "net.testiprod.pianoman.app.desktop"
            packageVersion = "1.0.0"
        }
    }
}
