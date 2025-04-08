@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "presentation"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.networking)
            implementation(projects.core.repositoryApi)
            implementation(projects.core.domain)
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.ui)
            api(compose.components.resources)

            api(libs.coroutines.core)
            api(libs.decompose)
            api(libs.decompose.jetbrains)
            api(libs.koin.core)
            api(libs.koin.compose)
            api(libs.kotlinx.serialization.json)
            api(libs.immutable.collections)
            api(libs.coil.compose)
            api(libs.coil.network.ktor)
            api(libs.coil.svg)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.padabajka.dating.core.presentation"
    compileSdk = libs.versions.projectConfig.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.projectConfig.minSdk.get().toInt()
    }
}

compose {
    resources {
        generateResClass = always
        publicResClass = true
    }
}
