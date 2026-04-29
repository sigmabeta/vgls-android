plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.sage.common.ui.icons)
}

android {
    namespace = "com.vgleadsheets.bottombar"
}
