plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.sage.di.android)
}

dependencies {
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    implementation(projects.vgls.android.pdf)
    implementation(libs.sage.android.ui.icons)
    implementation(projects.vgls.android.ui.list)
    implementation(projects.vgls.android.strings)

    implementation(libs.sage.common.appcomm)
    implementation(projects.vgls.common.urlinfo)

    // Only for previews
    implementation(projects.vgls.android.ui.theme)
}

android {
    namespace = "com.vgleadsheets.search"
}
