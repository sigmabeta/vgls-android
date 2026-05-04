plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.vgleadsheets.analytics"
}

dependencies {
    api(projects.vgls.common.analytics)
    api(libs.sage.android.analytics)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}
