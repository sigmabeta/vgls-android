plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)

    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.compose.android)
}

dependencies {
    implementation(projects.features.all)
    implementation(projects.features.navbar)
    implementation(projects.features.topbar)

    implementation(projects.vgls.common.model)
    implementation(libs.sage.common.ui.components)

    implementation(libs.sage.android.bitmaps)
    implementation(projects.vgls.android.images)
    implementation(projects.vgls.android.pdf)
    implementation(libs.sage.android.perf)
    implementation(projects.vgls.android.scaffold)
    implementation(projects.vgls.android.ui.components)
    implementation(libs.sage.android.ui.icons)
    implementation(projects.vgls.android.ui.list)
    implementation(libs.sage.android.ui.strings)
    implementation(libs.sage.android.ui.themes)
}

android {
    namespace = "com.vgleadsheets.ui.previews"
}
