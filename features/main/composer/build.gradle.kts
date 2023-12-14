plugins {
    alias(libs.plugins.vgls.feature.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(projects.core.android.list)
}

android {
    namespace = "com.vgleadsheets.features.main.composer"
}
