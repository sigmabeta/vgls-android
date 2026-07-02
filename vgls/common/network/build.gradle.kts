plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.network"
    }

    sourceSets {
        // Ktor + kotlinx.serialization API client + @Serializable models — multiplatform.
        named("commonMain") {
            dependencies {
                implementation(libs.sage.common.connectivity)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        // The @Inject fake generators (java.util.Random / okio-bound) stay JVM for now.
        named("jvmSharedMain") {
            dependencies {
                implementation(libs.sage.common.logging)
                implementation(projects.vgls.common.model)
                implementation(libs.sage.common.di)
                implementation(libs.okio)
                implementation(libs.ktor.client.mock)
            }
        }
    }
}
