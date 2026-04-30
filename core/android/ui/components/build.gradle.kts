plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
}

dependencies {
    implementation(projects.core.common.model)

    api(libs.sage.common.ui.components)

    implementation(libs.sage.android.bitmaps)
    implementation(projects.core.android.images)
    implementation(projects.core.android.pdf)
    implementation(libs.sage.android.perf)
    implementation(libs.sage.android.ui.icons)
    implementation(libs.sage.android.ui.themes)
    implementation(libs.sage.android.ui.strings)

    implementation(libs.kotlin.reflect)
}

android {
    namespace = "com.vgleadsheets.ui.components"
}
