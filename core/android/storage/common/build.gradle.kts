plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.dataStore.preferences)

    api(libs.sage.common.storage.common)
}

android {
    namespace = "com.vgleadsheets.storage.common"
}
