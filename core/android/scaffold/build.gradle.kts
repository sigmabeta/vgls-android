plugins {
    alias(libs.plugins.vgls.compose.android.module)
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(projects.core.android.ui.components)
    implementation(projects.core.android.viewmodel)

    implementation(projects.features.remaster.bottombar)
    implementation(projects.features.remaster.topbar)

    implementation(projects.features.remaster.browse)
    implementation(projects.features.remaster.composers.detail)
    implementation(projects.features.remaster.composers.list)
    implementation(projects.features.remaster.games.detail)
    implementation(projects.features.remaster.games.list)
    implementation(projects.features.remaster.home)
}

android {
    namespace = "com.vgleadsheets.scaffold"
}
