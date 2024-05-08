plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(projects.core.android.ui.icons)
    implementation(projects.core.android.ui.list)

    implementation(projects.core.common.settings.environment)
    implementation(projects.core.common.urlinfo)
}

android {
    namespace = "com.vgleadsheets.games.detail"
}
