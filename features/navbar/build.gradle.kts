plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.sage.di.android)
}

dependencies {
    implementation(libs.sage.common.ui.icons)
}

android {
    namespace = "com.vgleadsheets.bottombar"
}
