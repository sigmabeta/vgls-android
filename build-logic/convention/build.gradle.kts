import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.vgleadsheets.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.firebase.performance.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApp") {
            id = "vgleadsheets.android.app"
            implementationClass = "VglsAndroidAppPlugin"
            version = "1.0"
        }

        register("composeAndroidApp") {
            id = "vgleadsheets.compose.android.app"
            implementationClass = "VglsComposeAndroidAppPlugin"
            version = "1.0"
        }

        register("composeAndroidModule") {
            id = "vgleadsheets.compose.android.module"
            implementationClass = "VglsComposeAndroidModulePlugin"
            version = "1.0"
        }

        register("coreAndroidModule") {
            id = "vgleadsheets.core.android"
            implementationClass = "VglsCoreAndroidModulePlugin"
            version = "1.0"
        }

        register("coreJvmModule") {
            id = "vgleadsheets.core.jvm"
            implementationClass = "VglsCoreJvmModulePlugin"
            version = "1.0"
        }

        register("diAndroidModule") {
            id = "vgleadsheets.di.android"
            implementationClass = "VglsDiAndroidModulePlugin"
            version = "1.0"
        }

        register("diJvmModule") {
            id = "vgleadsheets.di.jvm"
            implementationClass = "VglsDiJvmModulePlugin"
            version = "1.0"
        }

        register("featureAndroidModule") {
            id = "vgleadsheets.feature.android"
            implementationClass = "VglsFeatureAndroidModulePlugin"
            version = "1.0"
        }

        register("featureAndroidComposeModule") {
            id = "vgleadsheets.feature.compose.android"
            implementationClass = "VglsFeatureAndroidComposeModulePlugin"
            version = "1.0"
        }
    }
}
