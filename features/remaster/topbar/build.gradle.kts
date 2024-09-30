plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(projects.core.android.ui.strings)

    // Only for previews
    implementation(projects.core.android.ui.themes)
}

android {
    namespace = "com.vgleadsheets.topbar"
}
