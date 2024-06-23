plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    implementation(libs.moshi)

    implementation(projects.core.common.appcomm)
    implementation(projects.core.common.coroutines)
    implementation(projects.core.common.logging)
    implementation(projects.core.common.model)
    implementation(projects.core.common.storage.common)

    ksp(libs.moshi.codegen)
}
