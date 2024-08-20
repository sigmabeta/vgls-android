import com.android.build.api.dsl.ApplicationBuildType

plugins {
    alias(libs.plugins.vgls.android.app)
    alias(libs.plugins.vgls.compose.android.app)
    alias(libs.plugins.vgls.di.android)

    alias(libs.plugins.git.version)
    alias(libs.plugins.gradle.publisher)
    alias(libs.plugins.licenses)
}

if (checkShouldIncludeFirebase()) {
    apply(plugin = "com.google.firebase.firebase-perf")
    apply(plugin = "com.google.firebase.crashlytics")
    apply(plugin = "com.google.gms.google-services")
}

val googlePlayTrack = System.getenv("TRACK")
if (googlePlayTrack != null) {
    apply(plugin = "com.github.triplet.play")

    play {
        track.set(googlePlayTrack)
        artifactDir.set(file("build/outputs/bundle/release/"))
    }
}

android {
    namespace = "com.vgleadsheets"

    defaultConfig {
        applicationId = "com.vgleadsheets"

        versionCode = 1
        versionName = "debug"

        testInstrumentationRunner = "com.vgleadsheets.VglsTestRunner"
    }

    signingConfigs {
        signingConfigs {
            val decodedPass = System.getenv("UPLOAD_KEYPASS")

            if (decodedPass != null) {
                create("release") {
                    keyAlias = System.getenv("UPLOAD_KEY")
                    keyPassword = decodedPass
                    storeFile = file("vgls-upload.jks")
                    storePassword = decodedPass
                }
            } else {
                create("release") {
                    keyAlias = "vgls-development"
                    keyPassword = "vgls-development"
                    storeFile = file("vgls-development.jks")
                    storePassword = "vgls-development"
                }
            }
        }
    }

    buildTypes {
        debug {
            addTimeToBuildConfig(butActuallyThough = false)
            addBranchNameToBuildConfig(butActuallyThough = false)

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
            addTimeToBuildConfig(butActuallyThough = true)
            addBranchNameToBuildConfig(butActuallyThough = true)

            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions {
        animationsDisabled = true
    }

    lint {
        checkDependencies = true
        ignoreTestSources = true
        checkReleaseBuilds = false
    }
}

dependencies {
    // DI dependencies
    implementation(projects.core.android.coroutines)
    implementation(projects.core.android.conversion)
    implementation(projects.core.android.database)
    implementation(projects.core.android.images)
    implementation(projects.core.android.logging)
    implementation(projects.core.android.pdf)
    implementation(projects.core.android.repository)
    implementation(projects.core.android.resources)
    implementation(projects.core.android.storage.common)
    implementation(projects.core.android.ui.core)
    implementation(projects.core.android.ui.strings)
    implementation(projects.core.android.ui.components)

    implementation(projects.core.common.appinfo)
    implementation(projects.core.common.downloader)
    implementation(projects.core.common.network)
    implementation(projects.core.common.settings.environment)
    implementation(projects.core.common.urlinfo)
    // End DI dependencies

    implementation(projects.features.remaster.all)

    implementation(libs.androidx.window.manager)
    implementation(libs.retrofit.moshi)

    // Debug helper
    implementation(libs.stetho)
    implementation(libs.stetho.okhttp)

    val shouldIncludeFirebase = checkShouldIncludeFirebase()
    logger.quiet("Including firebase: $shouldIncludeFirebase")
    if (shouldIncludeFirebase) {
        // Analytics must be in this module, or else contentprovider init doesn"t happen
        implementation(libs.firebase.analytics)
        implementation(libs.firebase.crashlytics)

        implementation(projects.core.android.firebase)
        implementation(projects.core.android.tracking)
    } else {
        implementation(projects.core.fake.perf)
        implementation(projects.core.fake.tracking)
    }

    // Memory leak detection (Uncomment to enable)
    // debugImplementation(LeakCanary.library)

    // Junit libs
    testImplementation(libs.junit4)
}

fun checkShouldIncludeFirebase(): Boolean {
    return File("app/google-services.json").exists()
}

fun ApplicationBuildType.addTimeToBuildConfig(butActuallyThough: Boolean) {
    val timeMs = if (butActuallyThough) {
        System.currentTimeMillis()
    } else {
        0L
    }

    buildConfigField("Long", "BUILD_TIME", "${timeMs}L")
}

fun ApplicationBuildType.addBranchNameToBuildConfig(butActuallyThough: Boolean) {
    val unknown = "Unknown"
    val branchEnvVariable = System.getenv("CIRCLE_BRANCH")

    val branchName = when {
        !butActuallyThough -> unknown
        branchEnvVariable.isNullOrEmpty() -> unknown
        else -> branchEnvVariable
    }

    buildConfigField("String", "BUILD_BRANCH", "\"$branchName\"")
}
