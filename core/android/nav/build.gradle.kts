plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.compose.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material3)

    api(projects.core.android.viewmodel)

    implementation(libs.sage.android.coroutines)

    api(libs.sage.common.appcomm)
    api(libs.sage.common.appinfo)

    implementation(projects.core.common.model)
    implementation(projects.core.common.notif)
    implementation(libs.sage.common.settings.general)
}

android {
    namespace = "com.vgleadsheets.nav"
}
