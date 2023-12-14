plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    implementation(projects.features.main.hud)

    implementation(projects.core.android.images)
    implementation(projects.core.android.recyclerview)
    implementation(projects.core.android.ui - components)
    implementation(projects.core.android.ui - core)

    implementation(projects.core.common.repository)
}