plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.scaffold"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                // The NavigationSuite type comes from the multiplatform nav-suite (base
                // material3-adaptive has no MP twin; NavigationUtils computes size from LocalWindowInfo).
                implementation(libs.jetbrains.compose.material3.adaptive.navigation.suite)
                implementation(libs.metrox.viewmodel)
                implementation(libs.metrox.viewmodel.compose)
                implementation(libs.sage.common.di)
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.transitions)

                implementation(projects.vgls.android.images.real)
                implementation(projects.vgls.android.licenses.real)
                implementation(projects.vgls.android.nav.real)
                implementation(libs.sage.common.ui.perfCompose)
                implementation(projects.vgls.android.ui.components.api)
                implementation(libs.sage.common.ui.iconsReal)
                implementation(projects.vgls.android.ui.list.api)
                implementation(projects.vgls.android.ui.theme.api)
                implementation(projects.vgls.android.viewmodel.real)
                implementation(projects.vgls.common.strings.api)

                implementation(libs.sage.common.pdf)

                implementation(projects.features.navbar.real)
                implementation(projects.features.topbar.real)

                implementation(projects.vgls.common.viewmodel.real)
                implementation(projects.features.home.real)
                implementation(projects.features.browse.real)
                implementation(projects.features.games.list.real)
                implementation(projects.features.games.detail.real)
                implementation(projects.features.composers.list.real)
                implementation(projects.features.composers.detail.real)
                implementation(projects.features.songs.list.real)
                implementation(projects.features.songs.detail.real)
                implementation(projects.features.favorites.real)
                implementation(projects.features.difficulty.list.real)
                implementation(projects.features.difficulty.values.real)
                implementation(projects.features.tags.list.real)
                implementation(projects.features.tags.values.real)
                implementation(projects.features.tags.songs.real)
                implementation(projects.features.menu.real)
                implementation(projects.features.updates.real)
                implementation(projects.features.offline.content.real)
                implementation(projects.features.offline.updates.real)
                implementation(projects.features.parts.real)
                implementation(projects.features.search.real)
                implementation(projects.features.viewer.real)
            }
        }
        named("androidMain") {
            dependencies {
                // RemasterBottomBar @Preview.
                implementation(libs.androidx.compose.ui.tooling.preview)
            }
        }
        named("jvmMain") {
            dependencies {
                // The JVM WithPerScreenViewModelStore actual hangs the per-screen ViewModelStore off a
                // Voyager ScreenModel (rememberScreenModel). Android's actual is a passthrough, so only
                // the desktop target needs voyager-screenmodel.
                implementation(libs.voyager.screenmodel)
            }
        }
    }
}
