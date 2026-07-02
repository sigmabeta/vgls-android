plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.viewmodel"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.androidx.lifecycle.viewmodel)
                api(libs.sage.common.analytics)
                api(libs.sage.common.coroutines)
                api(libs.sage.common.debug)
                api(libs.sage.common.list)
                api(projects.vgls.common.nav)
                api(libs.sage.common.perf)
                api(projects.vgls.common.repository)
                api(projects.vgls.common.urlinfo)
                implementation(libs.sage.common.ui.components)
                implementation(libs.sage.common.logging)
                implementation(libs.metrox.viewmodel)
                implementation(libs.sage.common.di)
            }
        }
    }
}
