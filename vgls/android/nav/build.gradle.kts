plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.compose.android)
    alias(libs.plugins.sage.di)
}

dependencies {
    implementation(libs.androidx.compose.material3)
    implementation(libs.metrox.viewmodel)
    // Voyager: NavViewModel drives the Navigator + returns Screens (public API -> api).
    api(libs.voyager.navigator)
    implementation(projects.vgls.common.nav)

    api(projects.vgls.android.viewmodel)

    implementation(libs.sage.android.coroutines)

    api(projects.vgls.common.appcomm)
    api(libs.sage.common.appinfo)

    implementation(projects.vgls.common.model)
    implementation(projects.vgls.common.notif)
    implementation(libs.sage.common.settings.general)
    implementation(projects.vgls.common.strings)
}

android {
    namespace = "com.vgleadsheets.nav"
}
