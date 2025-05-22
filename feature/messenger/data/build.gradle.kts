@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
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
            baseName = "messenger.data"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.data)
            api(projects.core.domain)
            api(projects.core.repositoryApi)
            implementation(projects.component.room)

            implementation(libs.room.runtime)
        }
        commonTest.dependencies {
            implementation(projects.testing)
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.padabajka.dating.feature.messenger.data"
    compileSdk = libs.versions.projectConfig.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.projectConfig.minSdk.get().toInt()
    }
}
