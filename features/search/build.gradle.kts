plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    implementation(projects.core.android.pdf)
    implementation(libs.sage.android.ui.icons)
    implementation(projects.core.android.ui.list)
    implementation(libs.sage.android.ui.strings)

    implementation(libs.sage.common.appcomm)
    implementation(projects.core.common.urlinfo)

    // Only for previews
    implementation(libs.sage.android.ui.themes)
}

android {
    namespace = "com.vgleadsheets.search"
}
