@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.mockmp)
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
            baseName = "ads"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.presentation)
            api(projects.core.domain)
            api(projects.core.repositoryApi)
            api(projects.core.data)
        }
        commonTest.dependencies {
            implementation(projects.testing)
            implementation(libs.kotlin.test)
        }
    }
}

mockmp {
    usesHelper = true
    installWorkaround()
}

android {
    namespace = "com.padabajka.dating.feature.ads"
    compileSdk = libs.versions.projectConfig.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.projectConfig.minSdk.get().toInt()
    }
}
