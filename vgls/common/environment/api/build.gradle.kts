plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.environment"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.sage.common.settings.environment)
            }
        }
    }
}
