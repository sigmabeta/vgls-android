plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
}

dependencies {
    implementation(projects.core.android.ui.themes)

    implementation(libs.sage.common.ui.icons)
}

android {
    namespace = "com.vgleadsheets.ui.icons"
}
