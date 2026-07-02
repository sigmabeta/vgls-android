plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
}

dependencies {
    // `api` (not implementation): StorageModule contributes a DataStore<Preferences> binding into the
    // AppScope Metro graph, so the type must be visible where the graph is aggregated (the :app module).
    api(libs.androidx.dataStore.preferences)

    api(libs.sage.common.storage.common)
}

android {
    namespace = "com.vgleadsheets.storage.common"
}
