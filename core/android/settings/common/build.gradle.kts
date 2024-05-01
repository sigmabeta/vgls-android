plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.dataStore.preferences)

    api(projects.core.common.settings.common)
}

android {
    namespace = "com.vgleadsheets.settings.common"
}
