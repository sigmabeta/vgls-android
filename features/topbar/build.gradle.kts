plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.topbar"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.strings)
                api(libs.sage.common.ui.components)
                api(libs.sage.common.appcomm)
                implementation(projects.vgls.android.ui.components)
                implementation(projects.vgls.common.nav.api)
                implementation(projects.vgls.android.viewmodel)
                implementation(projects.vgls.common.settings.part)
                implementation(libs.sage.common.coroutines)
                implementation(libs.sage.common.analytics)
                implementation(libs.sage.common.debug)
                implementation(libs.metrox.viewmodel)
                // Material back/menu icons for the top app bar (multiplatform).
                implementation(libs.jetbrains.compose.material.icons.extended)
                implementation(libs.sage.common.di)
            }
        }
        named("androidMain") {
            dependencies {
                // Previews only.
                implementation(projects.vgls.android.ui.theme)
                implementation(libs.androidx.compose.ui.tooling.preview)
            }
        }
    }
}
