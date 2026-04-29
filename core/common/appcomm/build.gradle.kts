plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    implementation(libs.moshi)

    implementation(libs.sage.common.logging)

    ksp(libs.moshi.codegen)
}
