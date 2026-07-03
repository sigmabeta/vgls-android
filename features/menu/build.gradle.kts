plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.remaster.menu"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.vgls.common.appcomm.api)
                api(libs.sage.common.appinfo)
                api(libs.sage.common.list)
                api(projects.vgls.common.model.api)
                api(projects.vgls.common.nav.api)
                api(projects.vgls.common.repository)
                api(projects.vgls.common.offline)
                api(libs.sage.common.settings.general)
                api(libs.sage.common.time)
                api(libs.sage.common.ui.components)
                api(projects.vgls.common.strings)
                api(projects.vgls.common.viewmodel)
                implementation(libs.metrox.viewmodel)
                implementation(libs.sage.common.di)
            }
        }
    }
}
