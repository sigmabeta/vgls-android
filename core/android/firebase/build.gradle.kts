plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    // Contains the PerfTracker interface
    api(projects.core.common.perf)

    // Firebase Perf
    api(libs.firebase.performance)

    implementation(projects.core.android.coroutines)

    // Dagger (important. include all 3 of these in modules that use Dagger
    implementation(projects.core.android.di)
}

android {
    namespace = "com.vgleadsheets.perf.tracking.firebase"
}
