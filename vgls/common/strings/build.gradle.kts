plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.common.strings"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(libs.sage.common.ui.strings)
                api(libs.sage.common.connectivity)
                implementation(libs.moshi)
            }
        }
    }
}
