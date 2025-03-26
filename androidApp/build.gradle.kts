plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.google.services)
    alias(libs.plugins.jetbrainsCompose)
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(projects.shared)
    implementation(compose.ui)
    implementation(compose.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.decompose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
