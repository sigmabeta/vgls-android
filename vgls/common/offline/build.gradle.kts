plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.common.offline"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.vgls.common.downloader)
                implementation(libs.sage.common.logging)
                implementation(projects.vgls.common.repository)
                implementation(libs.sage.common.time)
            }
        }
    }
}
