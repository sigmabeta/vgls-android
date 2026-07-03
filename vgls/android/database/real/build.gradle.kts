plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.ksp)
}

// KMP Room database (Room 2.7+). @Database/DAOs/entities shared in jvmSharedMain; KSP runs
// room-compiler per target (kspAndroid/kspJvm). Migrations use SupportSQLiteDatabase (androidMain).
// The DI that builds the DB lives in :vgls:android:database:di.
kotlin {
    android {
        namespace = "com.vgleadsheets.database.real"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.vgls.common.database.api)
                api(libs.room.runtime)
                implementation(projects.vgls.common.network.api)
            }
        }
        named("androidMain") {
            dependencies {
                implementation(libs.support.sqlite)
            }
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspJvm", libs.room.compiler)
}
