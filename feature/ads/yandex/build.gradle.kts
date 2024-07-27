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
            baseName = "ads.yandex"
            isStatic = true
        }

//        pod("YandexMobileAds") {
//            version = "6.4.1"
//            extraOpts += listOf("-compiler-option", "-fmodules")
//        }
    }

    sourceSets {
        commonMain.dependencies {
            // put your multiplatform dependencies here
            api(projects.core.presentation)
            api(projects.core.domain)
            api(projects.core.repositoryApi)
            api(projects.core.data)
        }
        androidMain.dependencies {
//            implementation(libs.yandex.ads)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.fp.padabajka.feature.ads.yandex"
    compileSdk = libs.versions.projectConfig.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.projectConfig.minSdk.get().toInt()
    }
}
