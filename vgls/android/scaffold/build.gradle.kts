plugins {
    alias(libs.plugins.sage.compose.android)
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
}

dependencies {
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.metrox.viewmodel)
    implementation(libs.metrox.viewmodel.compose)

    implementation(projects.vgls.android.images)
    implementation(projects.vgls.android.licenses)
    implementation(projects.vgls.android.nav)
    implementation(libs.sage.common.ui.perfCompose)
    implementation(projects.vgls.android.ui.components)
    implementation(libs.sage.common.ui.iconsReal)
    implementation(projects.vgls.android.ui.list)
    implementation(projects.vgls.android.ui.theme)
    implementation(projects.vgls.android.viewmodel)

    implementation(libs.sage.common.pdf)

    implementation(projects.features.navbar)
    implementation(projects.features.topbar)

    implementation(projects.features.search)
    implementation(projects.features.viewer)
}

android {
    namespace = "com.vgleadsheets.scaffold"
}
