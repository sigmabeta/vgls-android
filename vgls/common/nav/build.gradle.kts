plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.common.nav"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.sage.common.nav)
            }
        }
    }
}
