import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.gitlab.svg2ico.Svg2IcoTask
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.desktop.application.tasks.AbstractJPackageTask

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.svg2ico)
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
            implementation(libs.multiplatform.settings)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.toast4j)
            implementation(libs.jna)
            implementation(libs.jna.platform)
        }
        jvmTest.dependencies {
            implementation(libs.kotest.runner.junit5)
            implementation(libs.kotest.assertions.core)
            implementation(libs.kotest.property)

            implementation(compose.desktop.uiTestJUnit4)
            runtimeOnly(libs.junit.vintage.engine)
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

val appIconIco by tasks.registering(Svg2IcoTask::class) {
    source {
        sourcePath = file("src/commonMain/resources/app-icon.svg")
    }
    destination = project.layout.buildDirectory.file("converted-icons/app-icon.ico")
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
                iconFile.set(appIconIco.get().destination.get())
            }
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ClipNinja"
            packageVersion = version.toString()
        }
    }
}

// Works around lack of dependency from packageMsi to appIconIco
tasks.withType<AbstractJPackageTask>().configureEach { dependsOn(appIconIco) }

compose.resources {
    // It didn't seem to generate the Res file without this...
    generateResClass = always
}
