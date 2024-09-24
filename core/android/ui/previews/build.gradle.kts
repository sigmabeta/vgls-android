plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)

    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
}

dependencies {
    implementation(projects.features.remaster.all)

    implementation(projects.core.common.model)
    implementation(projects.core.common.ui.components)

    implementation(projects.core.android.animation)
    implementation(projects.core.android.bitmaps)
    implementation(projects.core.android.images)
    implementation(projects.core.android.pdf)
    implementation(projects.core.android.ui.core)
    implementation(projects.core.android.ui.icons)
    implementation(projects.core.android.ui.list)
    implementation(projects.core.android.ui.strings)
    implementation(projects.core.android.viewer)
}

android {
    namespace = "com.vgleadsheets.ui.previews"
}
