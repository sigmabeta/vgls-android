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

    implementation(projects.vgls.common.model.api)
    implementation(libs.kotlinx.datetime)
    implementation(libs.sage.common.ui.components)

    implementation(projects.vgls.android.bitmaps)
    implementation(projects.vgls.android.images)
    implementation(projects.vgls.android.pdf)
    implementation(libs.sage.common.ui.perfCompose)
    implementation(projects.vgls.android.scaffold)
    implementation(projects.vgls.android.ui.components)
    implementation(libs.sage.common.ui.iconsReal)
    implementation(libs.sage.common.ui.listScreens)
    implementation(projects.vgls.common.strings.api)
    implementation(libs.sage.android.ui.strings)
    implementation(projects.vgls.android.ui.theme)
}

android {
    namespace = "com.vgleadsheets.ui.previews"
}
