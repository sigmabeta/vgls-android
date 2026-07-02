plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.sage.di)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.moshi)

    implementation(libs.sage.common.appcomm)
    implementation(libs.sage.common.coroutines)
    implementation(libs.sage.common.logging)
    implementation(projects.vgls.common.model)
    implementation(projects.vgls.common.strings)
    implementation(libs.sage.common.storage.common)

    ksp(libs.moshi.codegen)
}
