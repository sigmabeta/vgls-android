plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(projects.core.common.ui.icons)
}

android {
    namespace = "com.vgleadsheets.bottombar"
}
