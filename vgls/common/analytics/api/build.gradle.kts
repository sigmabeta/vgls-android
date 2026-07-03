plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.analytics.api"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.sage.common.analytics)
            }
        }
    }
}
