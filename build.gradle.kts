plugins {
    // trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinCocoapods) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.detect)
}

detekt {
    toolVersion = libs.versions.detect.version.get()
    config.setFrom(file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true

    val paths = project.allprojects.map { it.projectDir }
    val files = files(paths)
    source.setFrom(files)
}

dependencies {
    detektPlugins(libs.detect.formatting)
}
