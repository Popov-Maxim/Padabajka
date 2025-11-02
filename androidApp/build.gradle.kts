plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.google.services)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.firebase.perf)
}

android {
    namespace = "com.padabajka.dating"
    compileSdk = libs.versions.projectConfig.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.padabajka.dating"
        minSdk = libs.versions.projectConfig.minSdk.get().toInt()
        targetSdk = libs.versions.projectConfig.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(projects.shared)
    implementation(projects.core.permission)
    implementation(compose.ui)
    implementation(libs.androidx.activity.compose)
    implementation(libs.decompose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.perf)
    implementation(libs.firebase.message)
    implementation(libs.koin.core)
}
