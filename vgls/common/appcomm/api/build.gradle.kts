plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.appcomm.api"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.sage.common.appcomm)
            }
        }
    }
}
