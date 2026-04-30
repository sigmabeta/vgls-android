plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    implementation(libs.coil.kt.core)
    implementation(libs.androidx.core.ktx)
    api(libs.zoomable.image.coil3)

    implementation(libs.sage.android.bitmaps)

    implementation(libs.sage.common.debug)
    implementation(projects.core.common.downloader)
    implementation(projects.core.common.repository)
    implementation(projects.core.common.urlinfo)
    api(libs.sage.common.pdf)
}

android {
    namespace = "com.vgleadsheets.pdf"
}
