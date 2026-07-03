import com.android.build.api.dsl.ApplicationBuildType

plugins {
    alias(libs.plugins.vgls.android.app)
    alias(libs.plugins.vgls.compose.android.app)
    alias(libs.plugins.sage.di)

    alias(libs.plugins.git.version)
    alias(libs.plugins.gradle.publisher)
//    alias(libs.plugins.licenses) //  No support for AGP 9 yet
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
            applicationIdSuffix = ".debug"

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

    testOptions {
        animationsDisabled = true
    }

    lint {
        checkDependencies = true
        ignoreTestSources = true
        checkReleaseBuilds = false
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.sage.common.time)
    implementation(libs.kotlinx.datetime)
    // DI dependencies
    implementation(projects.vgls.common.appcomm)
    implementation(libs.sage.android.connectivity)
    implementation(libs.sage.android.coroutines)
    implementation(projects.vgls.android.conversion)
    implementation(projects.vgls.android.database)
    implementation(projects.vgls.android.images)
    implementation(libs.sage.android.logging)
    implementation(projects.vgls.android.offline)
    implementation(projects.vgls.android.pdf)
    implementation(projects.vgls.android.repository)
    implementation(libs.sage.android.resources)
    implementation(projects.vgls.android.storage.common)
    implementation(projects.vgls.android.ui.components)
    implementation(projects.vgls.android.ui.theme)
    implementation(projects.vgls.common.strings)
    implementation(libs.sage.android.ui.strings)

    implementation(libs.sage.common.appinfo)
    implementation(projects.vgls.common.downloader)
    implementation(libs.sage.common.debug)
    implementation(libs.sage.common.events)
    implementation(projects.vgls.common.network)
    implementation(projects.vgls.common.offline)
    implementation(projects.vgls.common.environment)
    implementation(projects.vgls.common.urlinfo)
    implementation(projects.vgls.common.versions)
    // End DI dependencies

    implementation(projects.vgls.android.icons)
    implementation(projects.features.all)

    // Metro DI: the app owns VglsAppGraph (implements ActivityGraph from :vgls:android:activity),
    // instantiates WakeLockManagerImpl, and needs the metrox ViewModelGraph/factory types.
    implementation(projects.vgls.android.activity)
    implementation(projects.vgls.android.wakelocks.di)
    implementation(libs.metrox.viewmodel)

    // Metro aggregates @ContributesIntoMap ViewModels only from modules on the app graph's COMPILE
    // classpath. These VM-owning modules otherwise reach :app only via implementation-transitive deps
    // (so their contribution hints are invisible), which left their VMs out of the metroViewModel map
    // and crashed at runtime. Declare them directly, matching Chipbox's per-VM-module app deps.
    implementation(projects.vgls.android.nav)
    implementation(projects.vgls.android.scaffold)
    implementation(projects.vgls.android.licenses)
    implementation(projects.vgls.android.viewmodel)
    implementation(projects.features.topbar)
    implementation(projects.features.navbar)
    // WorkManager: VglsApplication provides a Configuration + VglsWorkerFactory (formerly via hilt-work).
    implementation(libs.androidx.work.manager)

    implementation(libs.androidx.window.manager)
    // Ktor HTTP client (replaces retrofit/okhttp/moshi). Engine + logging have no catalog alias.
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    val shouldIncludeFirebase = checkShouldIncludeFirebase()
    logger.quiet("Including firebase: $shouldIncludeFirebase")
    if (shouldIncludeFirebase) {
        // Analytics must be in this module, or else contentprovider init doesn"t happen
        implementation(libs.firebase.analytics)
        implementation(libs.firebase.crashlytics)

        implementation(libs.sage.android.firebase)
        implementation(libs.sage.android.analytics)
        implementation(projects.vgls.android.analytics.di)
    } else {
        implementation(libs.sage.common.perf)
        implementation(projects.vgls.common.analytics.fake)
    }

    // Memory leak detection (Uncomment to enable)
    // debugImplementation(LeakCanary.library)

    // Junit libs
    testImplementation(libs.junit4)

    // UI Testing
    debugImplementation(libs.androidx.compose.ui.testing.manifest)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.compose.ui.testing)
    // TODO(metro): androidTest DI (FakeApiModule/FakeImageLoaderBuilderModule) still uses Hilt
    //  @TestInstallIn — migrate to a Metro test graph before re-enabling connected tests.
}

appVersioning {
    releaseBuildOnly.set(true)

    overrideVersionCode { gitTag, _, _ ->
        println("Generating version code. Git tag: ${gitTag.rawTagName}")
        val tagSegments = gitTag.rawTagName.split('.')

        val major = tagSegments[0].toInt()
        val minor = tagSegments[1].toInt()
        val patch = tagSegments[2].toInt()

        val commits = gitTag.commitsSinceLatestTag

        val platType = 1 // replace with when statement if we ever support more than just mobile

        val branch = when (System.getenv("CIRCLE_BRANCH")) {
            "release" -> 9
            "beta" -> 8
            else -> 7
        }

        Versions.verifyRequirements("Major", major, Int.MAX_VALUE)
        Versions.verifyRequirements("Minor", minor, Versions.MAX_MINOR_VERSIONS)
        Versions.verifyRequirements("Patch", patch, Versions.MAX_PATCH_VERSIONS)
        Versions.verifyRequirements("Commit count", commits, Versions.MAX_COMMITS)
        Versions.verifyRequirements("Platform type", platType, Versions.MAX_PLAT_TYPES)
        Versions.verifyRequirements("Branch", branch, Versions.MAX_BRANCHES)

        major * Versions.MAJOR +
            minor * Versions.MINOR +
            patch * Versions.PATCH +
            commits * Versions.COMMIT +
            platType * Versions.PLAT_TYPE +
            branch * Versions.BRANCH
    }

    overrideVersionName { gitTag, _, _ ->
        val commits = gitTag.commitsSinceLatestTag
        "${gitTag.rawTagName}.$commits"
    }
}

fun checkShouldIncludeFirebase(): Boolean = File("app/google-services.json").exists()

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

object Versions {
    const val MAX_MINOR_VERSIONS = 10
    const val MAX_PATCH_VERSIONS = 100
    const val MAX_PLAT_TYPES = 10
    const val MAX_COMMITS = 100
    const val MAX_BRANCHES = 10

    const val MAJOR = 10_000_000
    const val MINOR = MAJOR / MAX_MINOR_VERSIONS
    const val PATCH = MINOR / MAX_PATCH_VERSIONS

    const val COMMIT = PATCH / MAX_COMMITS
    const val PLAT_TYPE = COMMIT / MAX_PLAT_TYPES
    const val BRANCH = PLAT_TYPE / MAX_BRANCHES

    fun verifyRequirements(type: String, actual: Int, max: Int) {
        require(actual < max) { "$type value $actual is equal to or greater than maximum $max." }

        println("Version component $type: $actual")
    }
}
