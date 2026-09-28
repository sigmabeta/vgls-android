plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.downloader.fake"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.vgls.common.downloader.api)
                implementation(libs.okio)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.mock)
                implementation(projects.vgls.common.model.api)
                implementation(projects.vgls.common.repository.real)
                implementation(projects.vgls.common.urlinfo.real)
                implementation(libs.sage.common.di)
            }
        }
    }
}
