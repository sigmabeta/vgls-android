plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.sage.di)
}

dependencies {
    api(projects.vgls.android.strings)

    implementation(libs.metrox.viewmodel)

    // Only for previews
    implementation(projects.vgls.android.ui.theme)
}

android {
    namespace = "com.vgleadsheets.topbar"
}
