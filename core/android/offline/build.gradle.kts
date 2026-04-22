plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.worker.android)
}

dependencies {
    implementation(projects.core.common.offline)
}

android {
    namespace = "com.vgleadsheets.offline"
}
