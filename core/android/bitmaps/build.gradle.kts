plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(projects.core.android.ui.fonts)
}

android {
    namespace = "com.vgleadsheets.bitmaps"
}
