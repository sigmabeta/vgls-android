plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
}

android {
    namespace = "com.vgleadsheets.themes"
}

dependencies {
    // Theming
    implementation(projects.core.android.ui.colors)
    api(projects.core.android.ui.fonts)
}
