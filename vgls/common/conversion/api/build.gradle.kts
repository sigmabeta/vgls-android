plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.common.conversion"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.model.api)
                implementation(libs.sage.common.coroutines)
            }
        }
        // network is pure-JVM (sage.jvm); FromNetwork.kt (the network->model mappers) stays here.
        named("jvmSharedMain") {
            dependencies {
                implementation(projects.vgls.common.network)
            }
        }
    }
}
