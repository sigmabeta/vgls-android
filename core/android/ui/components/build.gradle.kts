plugins {
    id("kotlin-kapt")
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
}

dependencies {
    implementation(projects.core.android.animation)
    implementation(projects.core.android.bitmaps)
    implementation(projects.core.android.images)
    implementation(projects.core.android.ui.core)
    implementation(projects.core.android.ui.icons)

    implementation(libs.kotlin.reflect)

    // For zoomable sheet images
    implementation(libs.photoview)
}

android {
    buildFeatures {
        dataBinding = true
    }

    namespace = "com.vgleadsheets.ui.components"
}
