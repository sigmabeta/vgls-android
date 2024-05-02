plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    implementation(projects.core.android.ui.core)
}

android {
    namespace = "com.vgleadsheets.ui.icons"
}
