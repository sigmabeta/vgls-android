plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.common.repository"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.model.api)
                api(libs.sage.common.settings.general)

                implementation(projects.vgls.common.appcomm.api)
                implementation(libs.sage.common.connectivity)
                implementation(projects.vgls.common.conversion.api)
                implementation(projects.vgls.common.database.api)
                implementation(libs.sage.common.logging)
                implementation(libs.sage.common.time)
                implementation(libs.sage.common.analytics)
                implementation(projects.vgls.common.strings)
            }
        }
        // network + notif are pure-JVM (sage.jvm) modules with no commonMain variant, so the files
        // using them (DbUpdater, UpdateManager, OfflineRepository, RandomRepository) stay here.
        named("jvmSharedMain") {
            dependencies {
                implementation(projects.vgls.common.network)
                implementation(projects.vgls.common.notif)
            }
        }
    }
}
