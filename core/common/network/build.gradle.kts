plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    // Module deps
    implementation(projects.core.common.logging)
    implementation(projects.core.common.model)
    implementation(projects.core.common.debug)

    // OkHttp libs
    api(libs.okhttp)
    api(libs.okhttp.logging)

    // Retrofit libs
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.moshi)
}

// See previous versions of this file for "GiantBombApiKey"
