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
include(":feature:auth:domain")
include(":feature:profile")
include(":feature:swiper")
include(":feature:match")
include(":feature:messenger")
include(":feature:image")
include(":feature:messenger:data")
include(":component:room")
include(":testing")
include(":feature:ads")
// include(":feature:ads:yandex")
include(":core:networking")
include(":core:utils")
include(":core:sync")
include(":feature:settings")
include(":feature:settings:metadata")
include(":feature:dictionary")
include(":feature:push")
include(":feature:permission-flow")
include(":feature:reaction-screen")
include(":core:platform:system-intent")
include(":core:platform:permission")
include(":core:platform:data")
