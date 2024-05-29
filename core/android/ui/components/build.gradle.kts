plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
}

dependencies {
    api(projects.core.common.ui.components)

    implementation(projects.core.android.animation)
    implementation(projects.core.android.bitmaps)
    implementation(projects.core.android.images)
    implementation(projects.core.android.pdf)
    implementation(projects.core.android.ui.core)
    implementation(projects.core.android.ui.icons)

    implementation(libs.kotlin.reflect)

    // For zoomable sheet images
    implementation(libs.photoview)
}

android {
    namespace = "com.vgleadsheets.ui.components"
}
