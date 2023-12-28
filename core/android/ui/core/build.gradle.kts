plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
    id("kotlin-parcelize")
}

dependencies {
    // Theming
    api(projects.core.android.ui.colors)
    api(projects.core.android.ui.themes)

    // Firebase Analytics
    api(projects.core.common.tracking)

    // Perf tracking (should be disabled in debug builds, see app/build.gradle.kts.kts)
    api(projects.core.common.perf)

    // To allow features to interact with MainActivity"s state.
    api(projects.core.android.nav)

    // All UI features should depend on Mavericks
    api(projects.core.android.mvrx)

    // AndroidX libs
    api(libs.androidx.appcompat)
    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.common)
    api(libs.androidx.constraint.layout)
    api(libs.material)
}

android {
    namespace = "com.vgleadsheets.ui.core"
}
