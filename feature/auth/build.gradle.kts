@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.mockmp)
    alias(libs.plugins.google.services)
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
            baseName = "auth"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core:presentation"))
            api(project(":core:domain"))
            api(project(":core:repository-api"))
            api(project(":core:data"))
            api(project(":feature:auth:domain"))

            implementation(libs.gitlive.firebase.auth)
        }
        commonTest.dependencies {
            implementation(project(":testing"))
            implementation(libs.kotlin.test)
        }
        val androidInstrumentedTest by getting
        androidInstrumentedTest.dependencies {
            implementation(libs.androidx.test.ext.junit)
            implementation(libs.espresso.core)
            implementation(libs.kotlin.test)
        }
    }
}

mockmp {
    usesHelper = true
    installWorkaround()
}

android {
    namespace = "com.fp.padabajka.feature.auth"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
