plugins {
    alias(libs.plugins.vgls.core.android)
    id("kotlin-kapt")
}

dependencies {
    // Dagger 2 Dependency Injection
    api(libs.dagger)
    api(libs.dagger.android)
}

android {
    namespace = "com.vgleadsheets.di"
}
