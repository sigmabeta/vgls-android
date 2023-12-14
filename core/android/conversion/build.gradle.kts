plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(projects.core.common.conversion)

    implementation(projects.core.android.database)

    implementation(projects.core.common.network)
}

android {
    namespace = "com.vgleadsheets.conversion"
}