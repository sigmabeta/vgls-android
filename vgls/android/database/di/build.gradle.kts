plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.database.di"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.vgls.android.database.real)
                implementation(projects.vgls.common.database.api)
                implementation(libs.sage.common.di)
            }
        }
        named("androidMain") {
            dependencies {
                // AndroidDatabaseModule builds the Room DBs with Context + Requery/framework SQLite.
                implementation(libs.support.sqlite)
                implementation(libs.sage.common.appinfo)
            }
        }
    }
}
