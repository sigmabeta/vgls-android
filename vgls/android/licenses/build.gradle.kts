plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
    alias(libs.plugins.sage.compose.android)
}

android {
    namespace = "com.vgleadsheets.licenses"
}

dependencies {
    implementation(libs.metrox.viewmodel)
    implementation(libs.metrox.viewmodel.compose)

    api(libs.webview)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.lifecycle.runtimeCompose)

    implementation(projects.vgls.android.ui.components)
    implementation(projects.vgls.android.strings)
    implementation(projects.vgls.android.viewmodel)

    api(libs.sage.common.appcomm)
    api(projects.vgls.common.nav)
}
