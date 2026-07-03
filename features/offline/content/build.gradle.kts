plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.remaster.offline.content"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.analytics.api)
                api(libs.sage.common.list)
                api(projects.vgls.common.model.api)
                api(projects.vgls.common.nav.api)
                api(projects.vgls.common.offline)
                api(libs.sage.common.pdf)
                api(projects.vgls.common.repository)
                api(libs.sage.common.ui.components)
                implementation(projects.vgls.common.strings)
                api(projects.vgls.common.viewmodel)
                implementation(libs.metrox.viewmodel)
                implementation(libs.sage.common.di)
            }
        }
    }
}
