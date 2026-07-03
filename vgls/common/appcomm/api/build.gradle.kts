plugins {
    alias(libs.plugins.sage.kmp)
    // sage.di's convention plugin does add("implementation", ...) which doesn't exist on a KMP
    // module, so apply the Metro compiler plugin directly and add sage.common.di to the source set
    // (what sage.di does under the hood), matching how Chipbox's KMP modules wire Metro.
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.appcomm"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.sage.common.appcomm)
                api(libs.sage.common.logging)
                implementation(libs.sage.common.di)
            }
        }
    }
}
