plugins {
    alias(libs.plugins.vgls.feature.android)
}

dependencies {
    implementation(projects.core.android.list)
}

android {
    namespace = "com.vgleadsheets.features.main.about"
}
