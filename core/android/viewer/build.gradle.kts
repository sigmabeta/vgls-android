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

    api(projects.core.common.nav)
    implementation(projects.core.android.ui.components)
    implementation(projects.core.android.viewmodel)

    implementation(projects.core.common.pdf)

    // T O D O these two deps are only necessary for previews. Can we somehow make them debug-only?
    implementation(libs.kotlinx.collections.immutable)
    implementation(projects.core.android.ui.themes)
}
