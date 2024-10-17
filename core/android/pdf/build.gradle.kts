plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    implementation(libs.coil.kt.core)
    implementation(libs.androidx.core.ktx)

    implementation(projects.core.android.bitmaps)

    implementation(projects.core.common.downloader)
    implementation(projects.core.common.repository)
    implementation(projects.core.common.urlinfo)
    api(projects.core.common.pdf)
}

android {
    namespace = "com.vgleadsheets.pdf"
}
