plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.sage.di)
}

dependencies {
    api(libs.sage.common.appcomm)
    api(libs.sage.common.logging)
}
