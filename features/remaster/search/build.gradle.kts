plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    implementation(projects.core.android.pdf)
    implementation(projects.core.android.ui.icons)

    implementation(projects.core.common.appcomm)
    implementation(projects.core.common.list)
    implementation(projects.core.common.urlinfo)
}

android {
    namespace = "com.vgleadsheets.search"
}
