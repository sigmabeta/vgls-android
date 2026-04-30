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

    api(libs.sage.common.nav)
    implementation(libs.sage.android.perf)
    implementation(projects.core.android.ui.components)
    implementation(libs.sage.android.ui.strings)
    implementation(projects.core.android.viewmodel)
}
