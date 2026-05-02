plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.compose.android)
}

dependencies {
    implementation(projects.vgls.common.appcomm)
    implementation(projects.vgls.common.model)

    api(libs.sage.common.ui.components)

    implementation(libs.sage.android.bitmaps)
    implementation(projects.vgls.android.images)
    implementation(projects.vgls.android.pdf)
    implementation(libs.sage.android.perf)
    implementation(libs.sage.android.ui.icons)
    implementation(libs.sage.android.ui.themes)
    implementation(libs.sage.android.ui.strings)

    implementation(libs.kotlin.reflect)
}

android {
    namespace = "com.vgleadsheets.ui.components"
}
