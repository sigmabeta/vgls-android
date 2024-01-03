plugins {
    alias(libs.plugins.vgls.compose.android.module)
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.navigation.compose)

    implementation(projects.core.common.repository)

    implementation(projects.core.android.ui.components)
    implementation(projects.core.android.ui.themes)

    api(projects.core.android.viewmodel)
}

android {
    namespace = "com.vgleadsheets.topbar"
}
