plugins {
    alias(libs.plugins.sage.kmp)
    // sage.di's convention plugin does add("implementation", ...) which doesn't exist on a KMP
    // module, so apply the Metro compiler plugin directly and add sage.common.di to the source set
    // (what sage.di does under the hood).
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.downloader"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.okio)
                implementation(libs.sage.common.connectivity)
                implementation(libs.sage.common.logging)
                implementation(libs.sage.common.pdf)
                implementation(projects.vgls.common.repository.real)
                implementation(projects.vgls.common.urlinfo.real)

                implementation(libs.sage.common.di)
            }
        }
        // The RealSheetDownloader reads the ktor HttpResponse the network SheetDownloadApi returns.
        named("jvmSharedMain") {
            dependencies {
                implementation(projects.vgls.common.network)
                implementation(libs.ktor.client.core)
            }
        }
    }
}
