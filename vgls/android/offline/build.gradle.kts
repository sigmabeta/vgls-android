plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.vgls.di.worker.android)
}

dependencies {
    implementation(projects.vgls.common.offline)
}

android {
    namespace = "com.vgleadsheets.offline"
}
