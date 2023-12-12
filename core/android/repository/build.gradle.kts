plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(projects.core.common.repository)

    implementation(projects.core.android.database)

    implementation(projects.core.common.coroutines)
    implementation(projects.core.common.conversion)
    implementation(projects.core.common.network)
    implementation(projects.core.common.tracking)
}

android {
    namespace = "com.vgleadsheets.repository"
}
