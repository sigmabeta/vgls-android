plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.notif"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.sage.common.appcomm)
                implementation(libs.sage.common.coroutines)
                implementation(libs.sage.common.logging)
                implementation(projects.vgls.common.strings)
                implementation(libs.sage.common.storage.common)
            }
        }
    }
}
