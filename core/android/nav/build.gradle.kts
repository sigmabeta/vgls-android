plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material3)

    implementation(projects.core.android.coroutines)
    implementation(projects.core.android.viewmodel)

    implementation(projects.core.common.appcomm)
    implementation(projects.core.common.model)
    implementation(projects.core.common.notif)
}

android {
    namespace = "com.vgleadsheets.nav"
}
