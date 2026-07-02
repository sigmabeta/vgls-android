plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.remaster.difficulty.values"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.analytics)
                api(libs.sage.common.images)
                api(libs.sage.common.list)
                api(projects.vgls.common.viewmodel)
                implementation(libs.metrox.viewmodel)
                api(projects.vgls.common.model)
                api(projects.vgls.common.nav)
                api(projects.vgls.common.repository)
                api(libs.sage.common.ui.components)
                api(projects.vgls.common.urlinfo)
                implementation(projects.vgls.common.strings)
                implementation(libs.sage.common.di)
            }
        }
    }
}
