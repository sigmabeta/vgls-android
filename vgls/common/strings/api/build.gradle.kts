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

// Paparazzi composeResources export. CMP packages androidMain composeResources as Android assets
// only — off the JVM unit-test classpath — so under Paparazzi VglsPreviewStrings' ClasspathResourceReader
// can't find the string `.cvr` and renders resource keys instead of values. Expose the composeResources
// as a classpath-shaped jar the screenshot module (:vgls:android:ui:previews:real) pulls as a test dep.
val composeResourcesElements: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}
val composeResourcesElementsJar = tasks.register<Jar>("composeResourcesElementsJar") {
    archiveClassifier.set("compose-resources")
    dependsOn("prepareComposeResourcesTaskForCommonMain")
    from(layout.buildDirectory.dir("generated/compose/resourceGenerator/preparedResources/commonMain/composeResources")) {
        into("composeResources/com.vgleadsheets.strings.generated.resources")
    }
}
artifacts {
    add(composeResourcesElements.name, composeResourcesElementsJar)
}
