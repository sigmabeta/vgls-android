plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.sage.di.jvm)
}

dependencies {
    implementation(libs.kotlin.stdlib)

    // Module deps
    implementation(libs.sage.common.connectivity)
    implementation(libs.sage.common.logging)
    implementation(projects.vgls.common.model)
    implementation(libs.kotlinx.coroutines.core)

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
