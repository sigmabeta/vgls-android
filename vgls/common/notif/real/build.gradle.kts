plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.notif.real"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.notif.api)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.sage.common.appcomm)
                implementation(libs.sage.common.coroutines)
                implementation(libs.sage.common.logging)
                implementation(projects.vgls.common.strings.api)
                implementation(libs.sage.common.storage.common)
            }
        }
    }
}
