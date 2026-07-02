plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
}

dependencies {
    api(libs.coil.kt.core)
    api(libs.coil.kt.compose)
    api(libs.coil.kt.okhttp)
    api(libs.kotlinx.collections.immutable)

    implementation(libs.sage.common.images)

    api(projects.vgls.android.bitmaps)
    implementation(libs.androidx.core.ktx)

    implementation(libs.sage.common.analytics)
    implementation(libs.sage.common.images)
    implementation(projects.vgls.common.model)
}

android {
    namespace = "com.vgleadsheets.images"
}
