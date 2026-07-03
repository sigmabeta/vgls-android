plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.search"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.androidx.lifecycle.runtimeCompose)
                implementation(projects.vgls.common.strings.api)
                implementation(projects.vgls.android.ui.components)
                implementation(projects.vgls.android.ui.list)
                implementation(projects.vgls.common.nav.api)
                implementation(projects.vgls.android.viewmodel)
                implementation(projects.vgls.android.pdf)
                implementation(projects.vgls.common.urlinfo.real)
                implementation(libs.sage.common.appcomm)
                implementation(libs.sage.common.ui.iconsApi)
                implementation(libs.metrox.viewmodel)
                implementation(libs.metrox.viewmodel.compose)
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
