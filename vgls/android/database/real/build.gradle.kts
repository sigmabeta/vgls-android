plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
    alias(libs.plugins.ksp)
}

// KMP Room database (Room 2.7+ multiplatform). The @Database classes + DAOs + entities are shared
// across Android and JVM in jvmSharedMain; KSP runs room-compiler against each target (kspAndroid /
// kspJvm) so the generated Room code lands per-platform. Android keeps the framework/Requery SQLite
// via the databaseBuilder(Context, …) overload (AndroidDatabaseModule); the JVM/desktop target uses
// the bundled SQLite driver (JvmDatabaseModule in apps/jvm). Migrations use SupportSQLiteDatabase, so
// they stay androidMain — a fresh desktop DB is created at the current schema and runs none.
kotlin {
    android {
        namespace = "com.vgleadsheets.database"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.vgls.common.database.api)
                api(libs.room.runtime)
                implementation(projects.vgls.common.network.api)
                implementation(libs.sage.common.di)
                implementation(libs.sage.common.appinfo)
            }
        }
        named("androidMain") {
            dependencies {
                // Requery/framework SupportSQLite open-helper + the SupportSQLiteDatabase migrations.
                implementation(libs.support.sqlite)
            }
        }
        // The JVM Room builder (BundledSQLiteDriver) lives in apps/jvm, where the desktop data
        // directory is known; the database module's jvm target just needs the shared @Database/DAOs.
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspJvm", libs.room.compiler)
}
