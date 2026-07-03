plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.ui.list"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                // lifecycle-runtime-compose (collectAsStateWithLifecycle) is multiplatform.
                api(libs.androidx.lifecycle.runtimeCompose)
                api(projects.vgls.common.nav.api)
                implementation(libs.sage.common.ui.perfCompose)
                implementation(libs.sage.common.ui.listScreens)
                implementation(projects.vgls.android.ui.components.api)
                implementation(projects.vgls.common.strings.api)
                // VglsListViewModel base, rendered by ListScreenContent (phase 4 plain-VM list screens).
                implementation(projects.vgls.common.viewmodel.real)
            }
        }
    }
}
