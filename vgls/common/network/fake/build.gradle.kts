plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.network.fake"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.vgls.common.network.api)
                implementation(projects.vgls.common.model.api)
                implementation(libs.sage.common.logging)
                implementation(libs.sage.common.di)
                implementation(libs.okio)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.mock)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
