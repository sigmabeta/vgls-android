plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    implementation(libs.kotlin.stdlib)

    // Module deps
    implementation(projects.core.common.logging)
    implementation(projects.core.common.model)
    implementation(projects.core.common.debug)

    // OkHttp libs
    api(libs.okhttp)
    api(libs.okhttp.logging)

    // Retrofit libs
    api(libs.retrofit.core)
    implementation(libs.retrofit.moshi)

    // Moshi code-gen
    ksp(libs.moshi.codegen)
}

// See previous versions of this file for "GiantBombApiKey"
