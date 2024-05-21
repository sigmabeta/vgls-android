plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(projects.core.android.ui.strings)
}

android {
    namespace = "com.vgleadsheets.topbar"
}
