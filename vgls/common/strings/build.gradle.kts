import org.jetbrains.compose.resources.ResourcesExtension

plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
    // VglsStringId is @Serializable (persisted inside NotifState via kotlinx.serialization).
    alias(libs.plugins.kotlin.serialization)
    // Compose Multiplatform plugin for the string-resource codegen (Res.allStringResources). This
    // is the SINGLE source of string values (src/commonMain/composeResources/values/strings.xml)
    // for every platform — replacing the old Android R.string + AndroidStringProvider path.
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.common.strings"

        // AGP 9's KMP android library ships with Android resource/asset processing OFF by default.
        // While off, the Compose Resources plugin's generated `.cvr` value resources never reach
        // the AAR/APK assets and the app crashes at runtime (JetBrains CMP-9547). Enable so they ship.
        androidResources {
            enable = true
        }
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.sage.common.ui.strings)
                implementation(libs.jetbrains.compose.resources)
                implementation(libs.kotlinx.serialization.core)
            }
        }
        // ImageLoadErrorStringId maps network exceptions -> VglsStringId; it is genuinely JVM-bound
        // (java.net/javax.net.ssl + sage connectivity), so it stays in the shared android+jvm set.
        named("jvmSharedMain") {
            dependencies {
                api(libs.sage.common.connectivity)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = ResourcesExtension.ResourceClassGeneration.Always
    packageOfResClass = "com.vgleadsheets.strings.generated.resources"
}
