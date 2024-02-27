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
            baseName = "match"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core:presentation"))
            api(project(":core:domain"))
            api(project(":core:data"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.fp.padabajka.feature.match"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}
