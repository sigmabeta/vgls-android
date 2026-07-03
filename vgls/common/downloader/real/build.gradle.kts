plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.downloader.real"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.vgls.common.downloader.api)
                implementation(libs.okio)
                implementation(libs.ktor.client.core)
                implementation(libs.sage.common.connectivity)
                implementation(libs.sage.common.logging)
                implementation(libs.sage.common.pdf)
                implementation(projects.vgls.common.repository.real)
                implementation(projects.vgls.common.urlinfo.real)
                implementation(projects.vgls.common.network.api)
                implementation(libs.sage.common.di)
            }
        }
    }
}
