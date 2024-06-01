plugins {
    alias(libs.plugins.vgls.core.android)
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

    debugImplementation(libs.kotlinx.collections.immutable)
    debugImplementation(projects.core.android.ui.themes)
}
