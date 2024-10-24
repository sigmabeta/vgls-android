plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(projects.core.common.perf)

    // Firebase Perf
    api(platform(libs.firebase.bom))
    api(libs.firebase.performance)

    implementation(projects.core.android.coroutines)
}

android {
    namespace = "com.vgleadsheets.perf.analytics.firebase"
}
