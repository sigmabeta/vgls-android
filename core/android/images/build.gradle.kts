plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(libs.coil.kt.core)
    api(libs.coil.kt.compose)
    api(libs.coil.kt.okhttp)
    api(libs.kotlinx.collections.immutable)

    implementation(projects.core.common.images)

    api(projects.core.android.bitmaps)

    implementation(projects.core.common.images)
    implementation(projects.core.common.model)
}

android {
    namespace = "com.vgleadsheets.images"
}
