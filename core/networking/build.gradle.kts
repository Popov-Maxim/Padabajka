plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
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
            baseName = "networking"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.repositoryApi)
            implementation(projects.core.domain)
            api(libs.koin.core)

            api(libs.ktor.core)
            api(libs.ktor.websockets)
            implementation(libs.ktor.cio)
            implementation(libs.ktor.auth)
            implementation(libs.ktor.content.negotiation)
            implementation(libs.ktor.serialization.json)
        }
        iosMain.dependencies {
            api(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.padabajka.dating.core.networking"
    compileSdk = libs.versions.projectConfig.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.projectConfig.minSdk.get().toInt()
    }
}
