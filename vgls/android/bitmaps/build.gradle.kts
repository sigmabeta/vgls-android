plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.vgleadsheets.bitmaps"
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.sage.android.bitmaps)
    implementation(projects.vgls.android.ui.theme)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}
