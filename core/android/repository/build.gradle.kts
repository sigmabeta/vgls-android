plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(projects.core.common.repository)
    api(libs.sage.common.appcomm)

    implementation(projects.core.android.database)

    implementation(libs.sage.common.coroutines)
    implementation(projects.core.common.conversion)
    implementation(projects.core.common.network)
    implementation(projects.core.common.notif)
    implementation(libs.sage.common.time)
    implementation(projects.core.common.ui.strings)
    implementation(projects.core.common.analytics)
}

android {
    namespace = "com.vgleadsheets.repository"
}
