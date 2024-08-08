import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.buildkonfig)
}

group = "garden.ephemeral.clipninja"
version = rootProject.file("version.txt").readText().trim()

kotlin {
    jvmToolchain(17)
    jvm {
        withJava()
    }
    sourceSets {
        commonMain.dependencies {
            implementation(compose.components.resources)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.toast4j)
            implementation(libs.jna)
        }
        jvmTest.dependencies {
            implementation(libs.kotest.runner.junit5)
            implementation(libs.kotest.assertions.core)
            implementation(libs.kotest.property)
        }
    }
}

buildkonfig {
    packageName = "garden.ephemeral.clipninja"

    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "CopyrightYears", "2024")
        buildConfigField(FieldSpec.Type.STRING, "OrganisationName", "Ephemeral Laboratories")
        buildConfigField(FieldSpec.Type.STRING, "ApplicationName", "ClipNinja")
        buildConfigField(FieldSpec.Type.STRING, "Version", version.toString())
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

compose.desktop {
    application {
        mainClass = "garden.ephemeral.clipninja.MainKt"
        nativeDistributions {
            windows {
                // FIXME: "Unknown publisher"
                upgradeUuid = "8248B478-A580-4AAB-BBEF-EECEE1ED46E4"
                menu = true
                menuGroup = "Utilities"
            }
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ClipNinja"
            packageVersion = version.toString()
        }
    }
}

compose.resources {
    // It didn't seem to generate the Res file without this...
    generateResClass = always
}
