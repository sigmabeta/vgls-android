plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(libs.androidx.lifecycle.viewModelCompose)

    api(libs.sage.common.analytics)
    api(libs.sage.common.coroutines)
    api(libs.sage.common.debug)
    api(libs.sage.common.list)
    api(libs.sage.common.nav)
    api(libs.sage.common.perf)
    api(projects.core.common.repository)
    api(projects.core.common.urlinfo)

    implementation(libs.sage.common.ui.components)
}

android {
    namespace = "com.vgleadsheets.viewmodel"
}
