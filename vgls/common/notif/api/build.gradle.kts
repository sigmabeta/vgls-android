plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.notif.api"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                // Notif's serialized fields reference VglsStringId + the sage GenericAction.
                api(projects.vgls.common.strings.api)
                api(libs.sage.common.appcomm)
            }
        }
    }
}
