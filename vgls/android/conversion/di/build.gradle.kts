plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.conversion.di"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.vgls.android.conversion.real)
                // The modules wire the Android datasources over the Room DAOs onto the DataSource interfaces.
                implementation(projects.vgls.android.database.real)
                implementation(projects.vgls.common.database.api)
                implementation(libs.sage.common.di)
            }
        }
    }
}
