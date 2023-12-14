plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    api(projects.core.android.ui.components)

    // AndroidX libs
    api(libs.androidx.recycler)
}

android {
    buildFeatures {
        dataBinding = true
    }
    namespace = "com.vgleadsheets.recyclerview"
}
