plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.vgleadsheets.ui.theme"
}

dependencies {
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material3)
    api(libs.material)
    api(libs.sage.android.ui.themes)
    implementation(libs.androidx.compose.ui.tooling.preview)
}
