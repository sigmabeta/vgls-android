plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.sage.di)
}

dependencies {
    api(projects.vgls.android.strings)

    implementation(libs.metrox.viewmodel)
    // Material back/menu icons for the top app bar (Icons.AutoMirrored.Default.ArrowBack, Icons.Default.Menu).
    implementation(libs.androidx.compose.material.icons.extended)

    // Only for previews
    implementation(projects.vgls.android.ui.theme)
}

android {
    namespace = "com.vgleadsheets.topbar"
}
