plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.common.analytics"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(libs.sage.common.analytics)
            }
        }
    }
}
