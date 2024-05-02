plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    // Contains the Tracker interface
    api(projects.core.common.tracking)

    // Firebase (Must be here or else we can"t log events
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    implementation(projects.core.android.coroutines)
}

android {
    namespace = "com.vgleadsheets.tracking"
}

