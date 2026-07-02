plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
}

android {
    namespace = "com.vgleadsheets.analytics"
}

dependencies {
    api(projects.vgls.common.analytics)
    api(libs.sage.android.analytics)
}
