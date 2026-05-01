plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di.android)
}

dependencies {
    api(projects.vgls.common.repository)
    api(libs.sage.common.appcomm)

    implementation(projects.vgls.android.database)

    implementation(libs.sage.common.coroutines)
    implementation(projects.vgls.common.conversion)
    implementation(projects.vgls.common.network)
    implementation(projects.vgls.common.notif)
    implementation(libs.sage.common.time)
    implementation(libs.sage.common.ui.strings)
    implementation(libs.sage.common.analytics)
}

android {
    namespace = "com.vgleadsheets.repository"
}
