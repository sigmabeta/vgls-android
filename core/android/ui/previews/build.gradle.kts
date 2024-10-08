plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)

    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
}

dependencies {
    implementation(projects.features.remaster.all)
    implementation(projects.features.remaster.navbar)
    implementation(projects.features.remaster.topbar)

    implementation(projects.core.common.model)
    implementation(projects.core.common.ui.components)

    implementation(projects.core.android.animation)
    implementation(projects.core.android.bitmaps)
    implementation(projects.core.android.images)
    implementation(projects.core.android.pdf)
    implementation(projects.core.android.perf)
    implementation(projects.core.android.scaffold)
    implementation(projects.core.android.ui.components)
    implementation(projects.core.android.ui.icons)
    implementation(projects.core.android.ui.list)
    implementation(projects.core.android.ui.strings)
    implementation(projects.core.android.ui.themes)
    implementation(projects.core.android.viewer)
}

android {
    namespace = "com.vgleadsheets.ui.previews"
}
