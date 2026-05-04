plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.vgleadsheets.analytics.fake"
}

dependencies {
    api(projects.vgls.common.analytics)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}
