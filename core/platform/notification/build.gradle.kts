import org.jetbrains.kotlin.gradle.dsl.JvmTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "notification"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
        }
        commonMain.dependencies {
            // TODO: Coil is only needed for Bitmap; remove Coil from the platform module.
            api(libs.coil.core)
            api(libs.koin.core)
        }
    }
}

android {
    namespace = "com.padabajka.dating.core.notification"
    compileSdk = libs.versions.projectConfig.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.projectConfig.minSdk.get().toInt()
    }
}
