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
            api(project(":core:presentation"))
            api(project(":core:domain"))
            api(project(":core:repository-api"))
            api(project(":core:data"))
        }
        commonTest.dependencies {
            implementation(project(":testing"))
            implementation(libs.kotlin.test)
        }
    }
}

mockmp {
    usesHelper = true
    installWorkaround()
}

android {
    namespace = "com.fp.padabajka.feature.ads"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}
