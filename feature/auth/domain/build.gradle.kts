import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    jvmToolchain(11)

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Auth Domain Module"
        homepage = "Link to the Auth Domain Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../../../iosApp/Podfile")
        framework {
            baseName = "auth.domain"
            isStatic = true
        }
        pod("GoogleSignIn")
        pod("GoogleUtilities")
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.domain)
            api(projects.core.repositoryApi)
            api(projects.feature.settings.metadata)
            implementation(libs.gitlive.firebase.auth)
        }
        androidMain.dependencies {
            implementation("androidx.credentials:credentials:1.3.0")
            implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
            implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
        }
        commonTest.dependencies {
            implementation(projects.testing)
            implementation(libs.kotlin.test)
        }
    }
}
private val localProps = Properties() // TODO: optimize config and add flavor release
rootProject.file("androidApp/config/debug.properties").inputStream().use { localProps.load(it) }

android {

    namespace = "com.padabajka.dating.feature.auth.domain"
    compileSdk = libs.versions.projectConfig.compileSdk.get().toInt()
    buildFeatures.buildConfig = true

    defaultConfig {
        minSdk = libs.versions.projectConfig.minSdk.get().toInt()
        buildConfigField(
            "String",
            "GOOGLE_CLIENT_ID",
            "\"${localProps["GOOGLE_CLIENT_ID"]}\""
        )
    }
}
