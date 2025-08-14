import org.jetbrains.kotlin.gradle.dsl.JvmTarget

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }

        pod("FirebaseAuth") {
            version = "10.19.0"
            linkOnly = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.ads)
//            implementation(projects.feature.ads.yandex)
            implementation(projects.feature.auth)
            implementation(projects.feature.profile)
            implementation(projects.feature.swiper)
            implementation(projects.feature.match)
            implementation(projects.feature.messenger)
            implementation(projects.feature.settings)
            implementation(projects.feature.settings.metadata)
            implementation(projects.feature.dictionary)
            implementation(projects.feature.push)
            implementation(projects.core.permission)
            implementation(projects.core.networking)
            implementation(projects.component.room)

            implementation(libs.gitlive.firebase.auth)
            implementation(libs.gitlive.firebase.perf)
            implementation(libs.gitlive.firebase.messaging)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.padabajka.dating"
    compileSdk = libs.versions.projectConfig.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.projectConfig.minSdk.get().toInt()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}
