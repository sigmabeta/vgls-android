plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.sage.di.android)
}

dependencies {
    api(libs.sage.android.ui.strings)

    // Only for previews
    implementation(libs.sage.android.ui.themes)
}

android {
    namespace = "com.vgleadsheets.topbar"
}
