plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
}

android {
    namespace = "com.vgleadsheets.wakelocks.di"
}

dependencies {
    api(projects.vgls.common.wakelocks.api)
    api(projects.vgls.android.wakelocks.real)

    // WakeLockModule's @Provides references EventDispatcher / SageDispatchers / StringProvider.
    implementation(libs.sage.common.appcomm)
    implementation(libs.sage.common.coroutines)
    implementation(libs.sage.common.ui.strings)
}
