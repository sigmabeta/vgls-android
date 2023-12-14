plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(libs.retrofit.core)

    implementation(projects.core.common.model)
    implementation(projects.core.common.perf)
    implementation(projects.core.common.repository)
    implementation(projects.core.common.tracking)

    implementation(projects.core.android.coroutines)
    implementation(projects.core.android.mvrx)
    implementation(projects.core.android.storage)
}

android {
    namespace = "com.vgleadsheets.nav"
}
