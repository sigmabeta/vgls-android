plugins {
    alias(libs.plugins.sage.kmp)
    // sage.di's convention plugin does add("implementation", ...) which doesn't exist on a KMP
    // module, so apply the Metro compiler plugin directly and add sage.common.di to the source set
    // (what sage.di does under the hood).
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.common.viewmodel"
    }

    sourceSets {
        named("jvmSharedMain") {
            dependencies {
                api(libs.androidx.lifecycle.viewmodel)

                api(libs.sage.common.list)
                api(libs.sage.common.debug)
                implementation(libs.sage.common.ui.components)

                implementation(libs.sage.common.di)
            }
        }
    }
}
