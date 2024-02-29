enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        mavenCentral()
    }
}

rootProject.name = "Padabajka"
include(":androidApp")
include(":shared")
include(":core:domain")
include(":core:repository-api")
include(":core:data")
include(":core:presentation")
include(":feature:auth")
include(":feature:profile")
include(":feature:swiper")
include(":feature:match")
