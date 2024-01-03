plugins {
    alias(libs.plugins.vgls.android.app)
    alias(libs.plugins.vgls.compose.android.app)
    alias(libs.plugins.vgls.di.android)
}

if (checkShouldIncludeFirebase()) {
    plugins {
        id("com.google.firebase.firebase-perf")
        id("com.google.firebase.crashlytics")
        id("com.google.gms.google-services")
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
            create("release") {
                val decodedPass = System.getenv("UPLOAD_KEYPASS")

                keyAlias = System.getenv("UPLOAD_KEY")
                keyPassword = decodedPass
                storeFile = file("vgls-upload.jks")
                storePassword = decodedPass
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
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

    packagingOptions {
        resources {
            excludes += listOf(
                "META-INF/mvrx_release.kotlin_module",
                "META-INF/common_debug.kotlin_module"
            )
        }
    }

    buildFeatures {
        dataBinding = true
    }

    testOptions {
        animationsDisabled = true
    }

    lint {
        checkDependencies = true
        ignoreTestSources = true
    }
}


dependencies {
    // DI dependencies only
    implementation(projects.core.android.coroutines)
    implementation(projects.core.android.conversion)
    implementation(projects.core.android.database)
    implementation(projects.core.common.debug)
    implementation(projects.core.android.images)
    implementation(projects.core.android.list)
    implementation(projects.core.android.logging)
    implementation(projects.core.common.network)
    implementation(projects.core.android.repository)
    implementation(projects.core.android.resources)
    implementation(projects.core.android.storage)
    implementation(projects.core.android.ui.core)
    implementation(projects.core.android.ui.strings)
    implementation(projects.core.android.ui.components)

    implementation(projects.core.android.activity)

    implementation(libs.androidx.window.manager)
    implementation(libs.retrofit.moshi)

    implementation(projects.features.remaster.home)

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
