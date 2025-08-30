import org.jetbrains.kotlin.gradle.dsl.JvmTarget

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.compose.compiler)
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
            baseName = "auth"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.presentation)
            api(projects.core.domain)
            api(projects.core.repositoryApi)
            api(projects.core.data)
            api(projects.feature.auth.domain)

            implementation(libs.gitlive.firebase.auth)
        }
        // TODO: delete after updating gitlive firebase
        androidMain.dependencies {
            implementation("com.google.firebase:firebase-bom:33.9.0")
            implementation("com.google.firebase:firebase-auth-ktx:23.2.1")
            implementation("com.google.firebase:firebase-auth:23.2.1")
            implementation("com.google.android.gms:play-services-auth:21.4.0")
        }
        commonTest.dependencies {
            implementation(projects.testing)
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

android {
    namespace = "com.padabajka.dating.feature.auth"
    compileSdk = libs.versions.projectConfig.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.projectConfig.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
