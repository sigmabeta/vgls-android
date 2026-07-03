plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.remaster.home"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.analytics.api)
                implementation(projects.vgls.common.appcomm.api)
                implementation(libs.sage.common.list)
                api(projects.vgls.common.viewmodel.real)
                implementation(libs.metrox.viewmodel)
                implementation(projects.vgls.common.offline.real)
                implementation(libs.sage.common.pdf)
                implementation(projects.vgls.common.repository.real)
                implementation(libs.sage.common.time)
                implementation(libs.sage.common.ui.components)
                implementation(projects.vgls.common.strings.api)
                implementation(projects.vgls.common.nav.api)
                implementation(libs.sage.common.di)
            }
        }
        // notif is pure-JVM (sage.jvm); HomeViewModel + the MostPlays*/Notif home modules stay here.
        named("jvmSharedMain") {
            dependencies {
                implementation(projects.vgls.common.notif.real)
            }
        }
    }
}
