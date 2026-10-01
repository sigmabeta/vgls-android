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

                implementation(projects.vgls.common.appcomm.real)
                implementation(libs.sage.common.connectivity)
                implementation(projects.vgls.common.conversion.api)
                implementation(projects.vgls.common.database.api)
                implementation(libs.sage.common.logging)
                implementation(libs.sage.common.time)
                implementation(libs.sage.common.analytics)
                implementation(projects.vgls.common.strings.api)
                implementation(projects.vgls.common.network.api)
                implementation(libs.sage.common.storage.common)
            }
        }
        // notif is a pure-JVM module with no commonMain variant, so the files using it stay here
        // (DbUpdater, UpdateManager, OfflineRepository, RandomRepository also use java.* APIs).
        named("jvmSharedMain") {
            dependencies {
                implementation(projects.vgls.common.notif.real)
            }
        }
    }
}
