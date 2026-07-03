plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.offline.real"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.offline.api)
                implementation(projects.vgls.common.downloader.api)
                implementation(libs.sage.common.logging)
                implementation(projects.vgls.common.repository.real)
                implementation(libs.sage.common.time)
            }
        }
    }
}
