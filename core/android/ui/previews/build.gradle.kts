plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
}

dependencies {
    implementation(projects.features.remaster.games.list)
    implementation(projects.features.remaster.games.detail)
    implementation(projects.features.remaster.songs.list)
    implementation(projects.features.remaster.songs.detail)
    implementation(projects.features.remaster.favorites)
    implementation(projects.features.remaster.search)

    implementation(projects.core.common.model)
    implementation(projects.core.common.ui.components)

    implementation(projects.core.android.animation)
    implementation(projects.core.android.bitmaps)
    implementation(projects.core.android.images)
    implementation(projects.core.android.pdf)
    implementation(projects.core.android.ui.core)
    implementation(projects.core.android.ui.icons)
    implementation(projects.core.android.ui.list)
    implementation(projects.core.android.ui.strings)
}

android {
    namespace = "com.vgleadsheets.ui.previews"
}
