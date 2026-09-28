plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.conversion.real"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.vgls.common.conversion.api)
                implementation(projects.vgls.android.database.real)
                implementation(projects.vgls.common.network.api)
            }
        }
    }
}
