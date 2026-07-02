plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
}

dependencies {
    api(projects.vgls.common.conversion)

    implementation(projects.vgls.android.database)

    implementation(projects.vgls.common.network)
}

android {
    namespace = "com.vgleadsheets.conversion"
}
