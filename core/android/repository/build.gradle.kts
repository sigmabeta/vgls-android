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
    implementation(libs.sage.common.ui.strings)
    implementation(libs.sage.common.analytics)
}

android {
    namespace = "com.vgleadsheets.repository"
}
