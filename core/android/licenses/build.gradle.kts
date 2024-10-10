plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
    alias(libs.plugins.vgls.compose.android.module)
}

android {
    namespace = "com.vgleadsheets.licenses"
}

dependencies {
    implementation(libs.androidx.hilt.navigation.compose)

    api(libs.webview)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.lifecycle.runtimeCompose)

    implementation(projects.core.android.ui.components)
    implementation(projects.core.android.viewmodel)

    api(projects.core.common.appcomm)
    api(projects.core.common.nav)
}
