plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
}

android {
    namespace = "com.vgleadsheets.ui.list"
}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.lifecycle.runtimeCompose)

    api(projects.core.common.nav)
    implementation(projects.core.android.ui.components)
    implementation(projects.core.android.ui.strings)
    implementation(projects.core.android.viewmodel)
}
