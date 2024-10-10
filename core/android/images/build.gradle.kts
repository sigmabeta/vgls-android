plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(libs.coil.kt)
    api(libs.coil.kt.compose)
    api(libs.kotlinx.collections.immutable)

    api(projects.core.android.bitmaps)

    implementation(projects.core.common.images)
    implementation(projects.core.common.model)
}

android {
    namespace = "com.vgleadsheets.images"
}
