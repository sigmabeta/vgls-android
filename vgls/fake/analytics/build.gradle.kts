plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.analytics.fake"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.vgls.common.analytics)
                implementation(libs.sage.common.di)
            }
        }
    }
}
