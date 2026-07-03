import org.jetbrains.compose.resources.ResourcesExtension

plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
    // JetBrains Compose Gradle plugin — needed for the `Res.font.*` codegen over the .otf under
    // `src/commonMain/composeResources/font/`, shared by Android + desktop.
    alias(libs.plugins.compose.multiplatform)
}

// The brand face (MuseJazz) as a Compose Multiplatform font resource, so BOTH Android and desktop
// load the same .otf via the generated `Res.font.*` accessors (replaces the old android-only
// R.font + a FontFamily.Default desktop stub).
kotlin {
    android {
        namespace = "com.vgleadsheets.ui.fonts.real"
        // AGP 9's KMP android library ships resource/asset processing OFF; while off, the generated
        // `Res.font.*` .otf never reaches the APK's `assets/composeResources/.../font/` and the app
        // crashes at runtime with MissingResourceException. Enabling assets packages them.
        androidResources {
            enable = true
        }
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.jetbrains.compose.resources)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = ResourcesExtension.ResourceClassGeneration.Always
    packageOfResClass = "com.vgleadsheets.ui.fonts.real.generated.resources"
}

// Paparazzi composeResources export. CMP packages androidMain composeResources as Android assets
// only — off the JVM unit-test classpath — so under Paparazzi the ClasspathResourceReader can't find
// the font `.cvr`/.otf and text falls back to the default face. Expose the composeResources as a
// classpath-shaped jar; the screenshot module (:vgls:android:ui:previews:real) pulls it as a test
// dep so the font lands on the unit-test classpath. Mirrors VglsPreviewStrings' reader for strings.
val composeResourcesElements: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}
val composeResourcesElementsJar = tasks.register<Jar>("composeResourcesElementsJar") {
    archiveClassifier.set("compose-resources")
    dependsOn("prepareComposeResourcesTaskForCommonMain")
    from(layout.buildDirectory.dir("generated/compose/resourceGenerator/preparedResources/commonMain/composeResources")) {
        into("composeResources/com.vgleadsheets.ui.fonts.real.generated.resources")
    }
}
artifacts {
    add(composeResourcesElements.name, composeResourcesElementsJar)
}
