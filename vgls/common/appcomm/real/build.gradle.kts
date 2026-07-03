plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.appcomm.real"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.appcomm.api)
                api(libs.sage.common.appcomm)
                api(libs.sage.common.logging)
                implementation(libs.sage.common.di)
            }
        }
    }
}
