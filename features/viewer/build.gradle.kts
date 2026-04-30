plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
    alias(libs.plugins.vgls.compose.android.module)
}

android {
    namespace = "com.vgleadsheets.viewer"
}

dependencies {
    api(libs.androidx.navigation.compose)
    api(libs.androidx.lifecycle.runtimeCompose)

    api(libs.sage.common.nav)
    implementation(libs.sage.android.bitmaps)
    implementation(projects.core.android.pdf)
    implementation(projects.core.android.ui.components)
    implementation(libs.sage.android.ui.icons)
    implementation(projects.core.android.viewmodel)

    implementation(libs.sage.common.pdf)
    api(libs.sage.android.wakelocks)

    // T O D O these two deps are only necessary for previews. Can we somehow make them debug-only?
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.sage.android.ui.themes)
}
