plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.remaster.parts"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.analytics)
                api(libs.sage.common.list)
                api(projects.vgls.common.model)
                api(projects.vgls.common.nav)
                api(projects.vgls.common.settings.part)
                api(libs.sage.common.ui.components)
                api(projects.vgls.common.strings)
                api(projects.vgls.common.viewmodel)
                implementation(libs.metrox.viewmodel)
                implementation(libs.sage.common.di)
            }
        }
    }
}
