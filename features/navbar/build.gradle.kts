plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.bottombar"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.vgls.common.strings)
                implementation(projects.vgls.common.nav.api)
                implementation(projects.vgls.android.viewmodel)
                implementation(libs.sage.common.ui.iconsApi)
                implementation(libs.sage.common.logging)
                implementation(libs.metrox.viewmodel)
                implementation(libs.sage.common.di)
            }
        }
    }
}
