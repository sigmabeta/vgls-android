plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.settings.part"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(libs.sage.common.coroutines)
                api(projects.vgls.common.model)
                api(libs.sage.common.storage.common)
            }
        }
    }
}
