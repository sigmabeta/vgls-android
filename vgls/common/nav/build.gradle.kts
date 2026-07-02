plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.common.nav"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(libs.sage.common.nav)
            }
        }
    }
}
