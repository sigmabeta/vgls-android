plugins {
    alias(libs.plugins.vgls.feature.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(projects.core.android.list)
    implementation(projects.core.android.ui.icons)
    implementation(projects.core.android.ui.strings)
}

android {
    namespace = "com.vgleadsheets.features.main.search"
}
