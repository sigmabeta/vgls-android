plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.common.conversion"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.vgls.common.model)
                implementation(libs.sage.common.coroutines)
                implementation(projects.vgls.common.network)
            }
        }
    }
}
