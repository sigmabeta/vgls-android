plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material3)

    api(projects.core.android.viewmodel)

    implementation(projects.core.android.coroutines)

    api(projects.core.common.appcomm)
    api(projects.core.common.appinfo)

    implementation(projects.core.common.model)
    implementation(projects.core.common.notif)
}

android {
    namespace = "com.vgleadsheets.nav"
}
