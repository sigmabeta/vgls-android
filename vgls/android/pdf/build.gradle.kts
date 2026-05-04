plugins {
    alias(libs.plugins.sage.android)
}

dependencies {
    implementation(libs.coil.kt.core)
    implementation(libs.androidx.core.ktx)
    api(libs.zoomable.image.coil3)

    implementation(libs.sage.android.bitmaps)
    implementation(projects.vgls.android.bitmaps)

    implementation(libs.sage.common.debug)
    implementation(projects.vgls.common.downloader)
    implementation(projects.vgls.common.repository)
    implementation(projects.vgls.common.urlinfo)
    api(libs.sage.common.pdf)
}

android {
    namespace = "com.vgleadsheets.pdf"
}
