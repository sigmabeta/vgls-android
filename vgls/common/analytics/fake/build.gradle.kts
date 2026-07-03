plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.analytics.fake"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.analytics.api)
            }
        }
        named("jvmSharedMain") {
            dependencies {
                implementation(libs.sage.common.di)
            }
        }
    }
}
