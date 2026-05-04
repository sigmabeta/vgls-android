plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.sage.di.android)
}

dependencies {
    api(projects.vgls.android.strings)

    // Only for previews
    implementation(projects.vgls.android.ui.theme)
}

android {
    namespace = "com.vgleadsheets.topbar"
}
