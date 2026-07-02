plugins {
    alias(libs.plugins.sage.compose.android)
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
}

dependencies {
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.metrox.viewmodel)
    implementation(libs.metrox.viewmodel.compose)
    // Voyager: the app's Navigator + all VglsScreen definitions live in this module now.
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.transitions)

    implementation(projects.vgls.android.images)
    implementation(projects.vgls.android.licenses)
    implementation(projects.vgls.android.nav)
    implementation(libs.sage.common.ui.perfCompose)
    implementation(projects.vgls.android.ui.components)
    implementation(libs.sage.common.ui.iconsReal)
    implementation(projects.vgls.android.ui.list)
    implementation(projects.vgls.android.ui.theme)
    implementation(projects.vgls.android.viewmodel)

    implementation(libs.sage.common.pdf)

    implementation(projects.features.navbar)
    implementation(projects.features.topbar)

    // Plain-ViewModel (phase 4) list screens: the feature VMs + the base type for the nav entries.
    implementation(projects.vgls.common.viewmodel)
    implementation(projects.features.home)
    implementation(projects.features.browse)
    implementation(projects.features.games.list)
    implementation(projects.features.games.detail)
    implementation(projects.features.composers.list)
    implementation(projects.features.composers.detail)
    implementation(projects.features.songs.list)
    implementation(projects.features.songs.detail)
    implementation(projects.features.favorites)
    implementation(projects.features.difficulty.list)
    implementation(projects.features.difficulty.values)
    implementation(projects.features.tags.list)
    implementation(projects.features.tags.values)
    implementation(projects.features.tags.songs)
    implementation(projects.features.menu)
    implementation(projects.features.updates)
    implementation(projects.features.offline.content)
    implementation(projects.features.offline.updates)
    implementation(projects.features.parts)

    implementation(projects.features.search)
    implementation(projects.features.viewer)
}

android {
    namespace = "com.vgleadsheets.scaffold"
}
