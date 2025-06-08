plugins {
    alias(libs.plugins.vgls.compose.android.module)
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(projects.core.android.images)
    implementation(projects.core.android.licenses)
    implementation(projects.core.android.nav)
    implementation(projects.core.android.perf)
    implementation(projects.core.android.ui.components)
    implementation(projects.core.android.ui.icons)
    implementation(projects.core.android.ui.list)
    implementation(projects.core.android.ui.themes)
    implementation(projects.core.android.viewmodel)

    implementation(projects.core.common.pdf)

    implementation(projects.features.navbar)
    implementation(projects.features.topbar)

    implementation(projects.features.search)
    implementation(projects.features.viewer)
}

android {
    namespace = "com.vgleadsheets.scaffold"
}
