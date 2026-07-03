plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
}

android {
    namespace = "com.vgleadsheets.storage.common.di"
}

dependencies {
    api(projects.vgls.android.storage.common.real)
    // StorageModule's @Provides reference SageDispatchers + Hatchet + CoroutineScope.
    implementation(libs.sage.common.coroutines)
    implementation(libs.sage.common.logging)
}
