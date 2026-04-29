plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    implementation(libs.moshi)

    implementation(libs.sage.common.appcomm)
    implementation(libs.sage.common.coroutines)
    implementation(libs.sage.common.logging)
    implementation(projects.core.common.model)
    implementation(libs.sage.common.ui.strings)
    implementation(libs.sage.common.storage.common)

    ksp(libs.moshi.codegen)
}
