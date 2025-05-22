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
            baseName = "auth.domain"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.domain)
            api(projects.core.repositoryApi)
            api(projects.feature.settings.metadata)
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
    namespace = "com.padabajka.dating.feature.auth.domain"
    compileSdk = libs.versions.projectConfig.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.projectConfig.minSdk.get().toInt()
    }
}
