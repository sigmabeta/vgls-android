plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.nav"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.voyager.navigator)
                api(projects.vgls.android.viewmodel)
                api(projects.vgls.common.appcomm)
                api(libs.sage.common.appinfo)
                api(projects.vgls.common.nav)
                implementation(libs.metrox.viewmodel)
                implementation(libs.sage.common.coroutines)
                implementation(projects.vgls.common.model)
                implementation(projects.vgls.common.notif)
                implementation(libs.sage.common.settings.general)
                implementation(projects.vgls.common.strings)
                implementation(libs.sage.common.di)
            }
        }
    }
}
