plugins {
    alias(libs.plugins.vgls.feature.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.viewpager)

    implementation(projects.core.android.animation)
    implementation(projects.core.android.coroutines)
    implementation(projects.core.android.images)
    implementation(projects.core.android.ui.components)
    implementation(projects.core.android.ui.icons)
}

android {
    namespace = "com.vgleadsheets.features.main.viewer"
}
