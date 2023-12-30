plugins {
    alias(libs.plugins.vgls.compose.android.module)
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.activity.compose)

    implementation(projects.core.android.scaffold)
    implementation(projects.core.android.ui.themes)
}

android {
    namespace = "com.vgleadsheets.activity"
}
