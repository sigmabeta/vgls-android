plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.remaster.updates"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.analytics.api)
                api(libs.sage.common.appinfo)
                api(libs.sage.common.list)
                api(projects.vgls.common.model.api)
                api(projects.vgls.common.nav.api)
                api(projects.vgls.common.repository.real)
                api(libs.sage.common.ui.components)
                api(projects.vgls.common.strings)
                api(libs.sage.common.time)
                api(projects.vgls.common.viewmodel)
                implementation(libs.metrox.viewmodel)
                implementation(libs.sage.common.di)
            }
        }
    }
}
