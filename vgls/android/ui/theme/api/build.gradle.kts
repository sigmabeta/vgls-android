plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.ui.theme"
        // AGP 9's KMP android library ships with resource/R processing OFF; enable it so the
        // androidMain res/ (XML themes + colors) generate an R class.
        androidResources {
            enable = true
        }
    }

    sourceSets {
        // Shared Compose theme (Colors/Typography/tokens + AppTheme wrapping Material3). material3
        // comes transitively from sage.compose.kmp. The brand face is loaded from the shared fonts
        // module (composeResources, both platforms); includeFontPadding is a platform seam
        // (expect/actual) in androidMain/jvmMain.
        named("commonMain") {
            dependencies {
                implementation(projects.vgls.android.ui.fonts.real)
            }
        }
        named("androidMain") {
            dependencies {
                api(libs.material)
                implementation(libs.androidx.compose.ui.tooling.preview)
            }
        }
    }
}
