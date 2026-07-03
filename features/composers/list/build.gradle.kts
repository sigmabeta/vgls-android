plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.remaster.composers.list"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.analytics.api)
                api(libs.sage.common.list)
                api(projects.vgls.common.viewmodel.real)
                implementation(libs.metrox.viewmodel)
                api(projects.vgls.common.model.api)
                api(projects.vgls.common.nav.api)
                api(projects.vgls.common.repository.real)
                api(libs.sage.common.ui.components)
                implementation(projects.vgls.common.strings.api)
                implementation(libs.sage.common.di)
            }
        }
    }
}
