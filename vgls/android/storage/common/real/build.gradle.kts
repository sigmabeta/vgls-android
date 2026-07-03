plugins {
    alias(libs.plugins.sage.android)
}

android {
    namespace = "com.vgleadsheets.storage.common.real"
}

dependencies {
    api(libs.androidx.dataStore.preferences)
    api(libs.sage.common.storage.common)
}
