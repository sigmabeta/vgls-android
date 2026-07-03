plugins {
    alias(libs.plugins.sage.android)
}

android {
    namespace = "com.vgleadsheets.analytics.real"
}

dependencies {
    api(projects.vgls.common.analytics.api)
    api(libs.sage.android.analytics)

    // VglsFirebaseAnalyticsImpl extends the sage FirebaseAnalyticsImpl and takes a FirebaseAnalytics;
    // sage.android.analytics keeps firebase as `implementation`, so declare it here. api() so the di
    // module (which @Provides this taking a FirebaseAnalytics) sees the type too.
    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.sage.common.coroutines)
}
