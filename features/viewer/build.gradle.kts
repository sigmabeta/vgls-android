plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di.android)
    alias(libs.plugins.sage.compose.android)
}

android {
    namespace = "com.vgleadsheets.viewer"
}

dependencies {
    api(libs.androidx.navigation.compose)
    api(libs.androidx.lifecycle.runtimeCompose)
    api(libs.sage.android.wakelocks)
    api(libs.sage.common.nav)

    implementation(projects.vgls.android.pdf)
    implementation(projects.vgls.android.ui.components)
    implementation(projects.vgls.android.viewmodel)
    implementation(projects.vgls.common.appcomm)

    implementation(libs.sage.android.bitmaps)
    implementation(libs.sage.android.ui.icons)
    implementation(libs.sage.common.pdf)
    implementation(libs.androidx.activity.compose)

    // TODO these two deps are only necessary for previews. Can we somehow make them debug-only?
    debugImplementation(libs.kotlinx.collections.immutable)
    debugImplementation(libs.sage.android.ui.themes)
}
