plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
}

android {
    namespace = "com.vgleadsheets.bitmaps"
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(projects.vgls.android.ui.theme)
}
