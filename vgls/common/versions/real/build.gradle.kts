plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.versions"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(libs.sage.common.appcomm)
                api(libs.sage.common.coroutines)
                api(libs.sage.common.logging)
                api(projects.vgls.common.notif.real)
                api(projects.vgls.common.repository.real)
                api(libs.sage.common.storage.common)
                api(projects.vgls.common.strings.api)

                implementation(projects.vgls.common.appcomm.api)
            }
        }
    }
}
