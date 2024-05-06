plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(projects.core.android.ui.icons)
    implementation(projects.core.android.ui.list)

    implementation(projects.core.common.urlinfo)
    implementation(projects.core.common.settings.environment)
}

android {
    namespace = "com.vgleadsheets.games.detail"
}
