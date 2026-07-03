plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.downloader.api"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.okio)
                api(libs.sage.common.pdf)
            }
        }
    }
}
