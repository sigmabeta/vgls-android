plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di.worker.android)
}

dependencies {
    implementation(projects.vgls.common.offline)
}

android {
    namespace = "com.vgleadsheets.offline"
}
