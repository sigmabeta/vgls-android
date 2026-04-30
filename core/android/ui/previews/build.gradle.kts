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

    implementation(projects.core.common.model)
    implementation(libs.sage.common.ui.components)

    implementation(libs.sage.android.bitmaps)
    implementation(projects.core.android.images)
    implementation(projects.core.android.pdf)
    implementation(libs.sage.android.perf)
    implementation(projects.core.android.scaffold)
    implementation(projects.core.android.ui.components)
    implementation(libs.sage.android.ui.icons)
    implementation(projects.core.android.ui.list)
    implementation(libs.sage.android.ui.strings)
    implementation(libs.sage.android.ui.themes)
}

android {
    namespace = "com.vgleadsheets.ui.previews"
}
