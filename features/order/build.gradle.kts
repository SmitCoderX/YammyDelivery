import java.io.FileInputStream
import java.util.Properties

val properties = Properties()
val propertiesFile: File = rootProject.file("apikey.properties")
properties.load(FileInputStream(propertiesFile))

plugins {
    id(PluginId.COMPOSE_CONVENTION)
    id(PluginId.KOTLIN_KAPT)
    id(PluginId.DAGGER_HILT)
    id(PluginId.KOTLIN_PARCELIZE)
}

android {
    defaultConfig {
        resValue("string", "google_key", properties.getProperty("GOOGLE_API_KEY"))
    }
    buildFeatures {
        resValues = true
    }
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.utils)
    implementation(projects.core.network)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.features.drawerApi)
    implementation(projects.services.update)
    implementation(projects.navigation)

    implementation(libs.android.core)
    implementation(libs.android.appcompat)

    implementation(libs.bundles.compose)
    implementation(libs.compose.icons)
    implementation(libs.compose.iconsExtended)
    implementation(libs.compose.viewModel)
    implementation(libs.compose.coil)
    implementation(libs.compose.navigationHilt)

    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pagerIndicators)

    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.viewModel)
    implementation(libs.lifecycle.runtime)
    kapt(libs.lifecycle.compiler)

    implementation(libs.hilt.library)
    kapt(libs.hilt.compiler)

    implementation(libs.navigation.ui)

    implementation(libs.google.maps)
    implementation(libs.google.mapsKtx)
    implementation(libs.google.location)
}