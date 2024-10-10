plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.coil.kt)

    implementation(projects.core.common.downloader)
    implementation(projects.core.common.repository)
    implementation(projects.core.common.urlinfo)
    api(projects.core.common.pdf)
}

android {
    namespace = "com.vgleadsheets.pdf"
}
