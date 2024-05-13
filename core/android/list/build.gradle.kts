plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(projects.core.android.coroutines)
    api(projects.core.android.ui.core)
    api(projects.core.android.ui.icons)
    api(projects.core.android.ui.components)
    api(projects.core.android.ui.strings)
}

android {
    buildFeatures {
        dataBinding = true
    }

    namespace = "com.vgleadsheets.features.main.list"

    kotlinOptions {
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
}
