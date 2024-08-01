import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.desktop.application.dsl.WindowsPlatformSettings

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    alias(libs.plugins.buildkonfig)
}

group = "garden.ephemeral.clipninja"
version = rootProject.file("version.txt").readText().trim()


repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.toast4j)
                implementation(libs.jna)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.junit.jupiter.api)
                implementation(libs.assertk)
                runtimeOnly(libs.junit.jupiter.engine)
            }
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
