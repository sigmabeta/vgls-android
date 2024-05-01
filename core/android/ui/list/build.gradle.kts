plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
}

android {
    namespace = "com.vgleadsheets.ui.list"
}

dependencies {
    implementation(projects.core.android.ui.components)
    implementation(projects.core.android.ui.core)
}
