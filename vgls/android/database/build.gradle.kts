plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di.android)
}

dependencies {
    api(projects.vgls.common.database)

    implementation(projects.vgls.common.network)

    // Room Libs
    api(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Support-SQLite
    implementation(libs.support.sqlite)
}

android {
    namespace = "com.vgleadsheets.database"

    buildFeatures {
        buildConfig = true
    }
}
