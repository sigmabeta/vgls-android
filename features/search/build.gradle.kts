plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.sage.di)
}

dependencies {
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.metrox.viewmodel)
    implementation(libs.metrox.viewmodel.compose)

    implementation(projects.vgls.android.pdf)
    implementation(libs.sage.common.ui.iconsReal)
    implementation(projects.vgls.android.ui.list)
    implementation(projects.vgls.common.strings)

    implementation(libs.sage.common.appcomm)
    implementation(projects.vgls.common.urlinfo)

    // Only for previews
    implementation(projects.vgls.android.ui.theme)
}

android {
    namespace = "com.vgleadsheets.search"
}
