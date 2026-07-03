plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.conversion"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.vgls.common.conversion.api)
                implementation(projects.vgls.android.database.real)
                implementation(projects.vgls.common.network.api)
                implementation(libs.sage.common.di)
            }
        }
    }
}
