plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.repository"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(projects.vgls.common.repository.real)
                api(projects.vgls.common.appcomm.api)
                api(libs.sage.common.appcomm)
                implementation(projects.vgls.android.database)
                implementation(libs.sage.common.coroutines)
                implementation(projects.vgls.common.conversion.api)
                implementation(projects.vgls.common.network.api)
                implementation(projects.vgls.common.notif.real)
                implementation(libs.sage.common.time)
                implementation(libs.sage.common.ui.strings)
                implementation(libs.sage.common.analytics)
                implementation(libs.sage.common.di)
            }
        }
    }
}
