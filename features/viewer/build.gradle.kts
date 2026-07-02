plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
    alias(libs.plugins.sage.compose.android)
}

android {
    namespace = "com.vgleadsheets.viewer"
}

dependencies {
    api(projects.vgls.common.analytics)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.lifecycle.runtimeCompose)
    api(projects.vgls.android.wakelocks)
    api(projects.vgls.common.nav)

    implementation(projects.vgls.android.pdf)
    implementation(projects.vgls.android.ui.components)
    implementation(projects.vgls.android.viewmodel)
    implementation(projects.vgls.common.appcomm)
    implementation(libs.metrox.viewmodel)
    implementation(libs.metrox.viewmodel.compose)

    implementation(projects.vgls.android.bitmaps)
    implementation(libs.sage.common.ui.iconsReal)
    implementation(libs.sage.common.pdf)
    implementation(libs.androidx.activity.compose)

    // TODO these two deps are only necessary for previews. Can we somehow make them debug-only?
    debugImplementation(libs.kotlinx.collections.immutable)
    debugImplementation(projects.vgls.android.ui.theme)
    implementation(projects.vgls.common.strings)
}
