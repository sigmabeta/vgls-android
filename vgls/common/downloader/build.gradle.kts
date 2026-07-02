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
                implementation(libs.sage.common.connectivity)
                implementation(libs.sage.common.logging)
                implementation(libs.sage.common.pdf)
                implementation(projects.vgls.common.repository)
                implementation(projects.vgls.common.urlinfo)

                implementation(libs.sage.common.di)
            }
        }
        // network is pure-JVM (sage.jvm); the downloader impls that use it stay here.
        named("jvmSharedMain") {
            dependencies {
                implementation(projects.vgls.common.network)
            }
        }
    }
}
