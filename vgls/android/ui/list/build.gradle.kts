plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.compose.android)
}

android {
    namespace = "com.vgleadsheets.ui.list"
}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.lifecycle.runtimeCompose)

    api(projects.vgls.common.nav)
    implementation(libs.sage.android.perf)
    implementation(libs.sage.android.ui.list)
    implementation(projects.vgls.android.ui.components)
    implementation(projects.vgls.android.strings)
    implementation(projects.vgls.android.viewmodel)
}
