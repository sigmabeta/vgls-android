plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(projects.core.android.ui.icons)
    implementation(projects.core.android.ui.list)
}

android {
    namespace = "com.vgleadsheets.composers.list"
}
