plugins {
    alias(libs.plugins.vgls.feature.compose.android)
    alias(libs.plugins.vgls.di.android)
}

android {
    buildFeatures {
        viewBinding = true
    }

    namespace = "com.vgleadsheets.features.main.hud"
}

dependencies {
    implementation(projects.core.common.model)
    implementation(projects.core.common.repository)

    implementation(projects.core.android.animation)
    implementation(projects.core.android.coroutines)
    implementation(projects.core.android.images)
    implementation(projects.core.android.nav)
    implementation(projects.core.android.recyclerview)
    implementation(projects.core.android.storage)
    implementation(projects.core.android.ui.core)
    implementation(projects.core.android.ui.components)
    implementation(projects.core.android.ui.icons)
}
