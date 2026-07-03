plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.urlinfo.real"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.urlinfo.api)
                api(libs.sage.common.coroutines)
                api(libs.sage.common.settings.general)
                api(libs.sage.common.settings.environment)
                api(projects.vgls.common.settings.part.real)
            }
        }
    }
}
