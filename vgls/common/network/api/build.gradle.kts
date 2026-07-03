plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.network.api"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.ktor.client.core)
                implementation(libs.sage.common.connectivity)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}
