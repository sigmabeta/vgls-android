plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di.android)
    alias(libs.plugins.sage.compose.android)
}

android {
    namespace = "com.vgleadsheets.licenses"
}

dependencies {
    implementation(libs.androidx.hilt.navigation.compose)

    api(libs.webview)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.lifecycle.runtimeCompose)

    implementation(projects.vgls.android.ui.components)
    implementation(libs.sage.android.ui.strings)
    implementation(projects.vgls.android.viewmodel)

    api(libs.sage.common.appcomm)
    api(libs.sage.common.nav)
}
