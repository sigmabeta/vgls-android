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

                implementation(projects.vgls.android.images)
                implementation(projects.vgls.android.licenses)
                implementation(projects.vgls.android.nav)
                implementation(libs.sage.common.ui.perfCompose)
                implementation(projects.vgls.android.ui.components.api)
                implementation(libs.sage.common.ui.iconsReal)
                implementation(projects.vgls.android.ui.list.api)
                implementation(projects.vgls.android.ui.theme.api)
                implementation(projects.vgls.android.viewmodel)
                implementation(projects.vgls.common.strings.api)

                implementation(libs.sage.common.pdf)

                implementation(projects.features.navbar)
                implementation(projects.features.topbar)

                implementation(projects.vgls.common.viewmodel.real)
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
