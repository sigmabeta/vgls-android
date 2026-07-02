plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.common.database"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.sage.common.coroutines)
                api(projects.vgls.common.model)
            }
        }
    }
}
