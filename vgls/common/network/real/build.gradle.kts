plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.network.real"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.vgls.common.network.api)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
