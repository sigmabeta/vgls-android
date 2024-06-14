plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)


    implementation(projects.core.android.ui.icons)
}

android {
    namespace = "com.vgleadsheets.search"
}
