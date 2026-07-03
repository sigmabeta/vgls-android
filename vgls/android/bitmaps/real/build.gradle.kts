plugins {
    alias(libs.plugins.sage.kmp)
    // The android-graphics bitmap generators are @Inject/@SingleIn(AppScope) — apply Metro directly
    // (sage.di's convention plugin does add("implementation", ...) which doesn't exist on KMP) and
    // add sage.common.di to androidMain.
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.bitmaps"
        // The androidMain generators read R.drawable.img_leadsheet_single_system_blank; AGP9's KMP
        // android library defaults resource/R processing OFF, so enable it.
        androidResources {
            enable = true
        }
    }

    sourceSets {
        // Pure sheet-geometry constants + color/scaling helpers used by the commonMain UI.
        // The actual android.graphics.Bitmap/Canvas rendering lives in androidMain (Coil fetcher
        // plumbing registered at the app level; desktop simply doesn't register it).
        named("androidMain") {
            dependencies {
                implementation(libs.androidx.core.ktx)
                implementation(projects.vgls.android.ui.theme.api)
                implementation(projects.vgls.android.ui.fonts.real)
                implementation(libs.sage.common.logging)
                implementation(libs.sage.common.di)
            }
        }
    }
}
