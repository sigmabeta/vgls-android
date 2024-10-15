plugins {
    alias(libs.plugins.vgls.compose.android.module)
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splash)

    implementation(projects.core.android.images)
    implementation(projects.core.android.nav)
    implementation(projects.core.android.perf)
    implementation(projects.core.android.scaffold)
    implementation(projects.core.android.ui.themes)

    implementation(projects.core.common.versions)
}

android {
    namespace = "com.vgleadsheets.activity"
}
