plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
}

android {
    namespace = "com.vgleadsheets.analytics.di"
}

dependencies {
    api(projects.vgls.common.analytics.api)
    // real api-exposes sage.android.analytics + firebase, so VglsAnalyticsModule sees FirebaseAnalytics,
    // the sage Analytics interface, and VglsFirebaseAnalyticsImpl.
    api(projects.vgls.android.analytics.real)

    // VglsAnalyticsModule's @Provides also references SageDispatchers + CoroutineScope.
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.sage.common.coroutines)
}
