plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    implementation(libs.androidx.dataStore.preferences)

    api(projects.core.common.environment)
}

android {
    namespace = "com.vgleadsheets.environments"
}
