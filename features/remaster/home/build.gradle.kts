plugins {
    alias(libs.plugins.vgls.compose.android.module)
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.navigation.compose)

    implementation(projects.core.android.ui.components)
    implementation(projects.core.android.ui.themes)
    implementation(projects.core.android.viewmodel)

    implementation(projects.core.common.repository)
}

android {
    namespace = "com.vgleadsheets.home"
}
