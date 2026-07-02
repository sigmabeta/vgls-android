plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
}

android {
    namespace = "com.vgleadsheets.analytics.fake"
}

dependencies {
    api(projects.vgls.common.analytics)
}
