plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.sage.di)
}

dependencies {
    implementation(libs.metrox.viewmodel)
    implementation(libs.sage.common.ui.iconsReal)
    implementation(projects.vgls.common.strings)
}

android {
    namespace = "com.vgleadsheets.bottombar"
}
