plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.sage.di.jvm)
}

dependencies {
    implementation(libs.moshi)

    implementation(libs.sage.common.appcomm)
    implementation(libs.sage.common.coroutines)
    implementation(libs.sage.common.logging)
    implementation(projects.vgls.common.model)
    implementation(libs.sage.common.ui.strings)
    implementation(libs.sage.common.storage.common)

    ksp(libs.moshi.codegen)
}
