plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(projects.core.common.repository)
    api(projects.core.common.appcomm)

    implementation(projects.core.android.database)

    implementation(projects.core.common.coroutines)
    implementation(projects.core.common.conversion)
    implementation(projects.core.common.network)
    implementation(projects.core.common.notif)
    implementation(projects.core.common.time)
    implementation(projects.core.common.analytics)
}

android {
    namespace = "com.vgleadsheets.repository"
}
