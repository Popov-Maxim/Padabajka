plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
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
            baseName = "dictionary"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.presentation)
            api(projects.core.domain)
            api(projects.core.repositoryApi)
            api(projects.core.data)
            api(projects.core.networking)
        }
    }
}

android {
    namespace = "com.padabajka.dating.feature.dictionary"
    compileSdk = libs.versions.projectConfig.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.projectConfig.minSdk.get().toInt()
    }
}
