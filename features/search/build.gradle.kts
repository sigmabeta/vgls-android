plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    implementation(projects.vgls.android.pdf)
    implementation(libs.sage.android.ui.icons)
    implementation(projects.vgls.android.ui.list)
    implementation(libs.sage.android.ui.strings)

    implementation(libs.sage.common.appcomm)
    implementation(projects.vgls.common.urlinfo)

    // Only for previews
    implementation(libs.sage.android.ui.themes)
}

android {
    namespace = "com.vgleadsheets.search"
}
