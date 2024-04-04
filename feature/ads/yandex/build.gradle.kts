@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
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

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Yandex Ads Module"
        homepage = "Link to the Yandex Ads Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../../../iosApp/Podfile")
        framework {
            baseName = "yandex"
            isStatic = true
        }

        pod("YandexMobileAds") {
            version = "6.4.1"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    sourceSets {
        commonMain.dependencies {
            // put your multiplatform dependencies here
            api(project(":core:presentation"))
            api(project(":core:domain"))
            api(project(":core:repository-api"))
            api(project(":core:data"))
        }
        androidMain.dependencies {
            implementation(libs.yandex.ads)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.fp.padabajka.feature.ads.yandex"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}
