plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(libs.simplestore)

    implementation(projects.core.android.coroutines)
    implementation(libs.kotlinx.coroutines.guava)

    // Common dependencies
    implementation(projects.core.common.debug)
}

android {
    namespace = "com.vgleadsheets.storage"
}
