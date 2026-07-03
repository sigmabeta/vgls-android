plugins {
    alias(libs.plugins.sage.compose.android)
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splash)

    implementation(projects.vgls.android.icons)
    implementation(projects.vgls.android.images)
    implementation(projects.vgls.android.nav)
    implementation(libs.sage.common.ui.perfCompose)
    implementation(projects.vgls.android.pdf)
    implementation(projects.vgls.android.scaffold)
    implementation(projects.vgls.android.ui.theme)

    // LocalVglsStringProvider (provided at the Compose root) + StringProvider type.
    implementation(projects.vgls.common.strings.api)

    implementation(projects.vgls.common.versions.real)

    // MetroViewModelFactory (ActivityGraph) + LocalMetroViewModelFactory (setContent).
    implementation(libs.metrox.viewmodel)
    implementation(libs.metrox.viewmodel.compose)
}

android {
    namespace = "com.vgleadsheets.activity"
}
