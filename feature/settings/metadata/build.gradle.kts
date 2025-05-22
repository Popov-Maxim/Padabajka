plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
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
            baseName = "settings.metadata"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.domain)
            api(projects.core.repositoryApi)
            api(projects.core.data)
            api(projects.core.networking)

            implementation(libs.gitlive.firebase.installations)
        }
    }
}

android {
    namespace = "com.padabajka.dating.settings.metadata"
    compileSdk = libs.versions.projectConfig.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.projectConfig.minSdk.get().toInt()
    }
}
