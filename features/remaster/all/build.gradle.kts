plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(projects.core.android.activity)

    api(projects.core.common.list)
    api(projects.core.common.nav)

    // List features here.
    api(projects.features.remaster.home)
    api(projects.features.remaster.browse)
    api(projects.features.remaster.games.list)
    api(projects.features.remaster.games.detail)
}

android {
    namespace = "com.vgleadsheets.features.all"
}
