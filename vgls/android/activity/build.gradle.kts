plugins {
    alias(libs.plugins.sage.compose.android)
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di.android)
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splash)

    implementation(projects.vgls.android.icons)
    implementation(projects.vgls.android.images)
    implementation(projects.vgls.android.nav)
    implementation(libs.sage.android.perf)
    implementation(projects.vgls.android.pdf)
    implementation(projects.vgls.android.scaffold)
    implementation(projects.vgls.android.ui.theme)

    implementation(projects.vgls.common.versions)
}

android {
    namespace = "com.vgleadsheets.activity"
}
