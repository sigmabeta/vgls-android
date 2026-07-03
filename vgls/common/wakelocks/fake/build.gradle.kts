plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.wakelocks.fake"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.wakelocks.api)
            }
        }
        named("jvmSharedMain") {
            dependencies {
                implementation(libs.sage.common.di)
            }
        }
    }
}
