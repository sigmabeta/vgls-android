plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(projects.core.common.database)

    implementation(projects.core.common.network)

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
