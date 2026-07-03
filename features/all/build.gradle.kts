plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.di)
}

dependencies {
    api(projects.vgls.android.activity.real)

    api(libs.sage.common.list)
    api(projects.vgls.common.offline.real)
    api(projects.vgls.common.nav.api)
    api(projects.vgls.common.notif.real)

    // List features here.
    api(projects.features.home)
    api(projects.features.browse)
    api(projects.features.search)
    api(projects.features.games.list)
    api(projects.features.games.detail)
    api(projects.features.composers.list)
    api(projects.features.composers.detail)
    api(projects.features.favorites)
    api(projects.features.menu)
    api(projects.features.offline.content)
    api(projects.features.offline.updates)
    api(projects.features.parts)
    api(projects.features.songs.list)
    api(projects.features.songs.detail)
    api(projects.features.difficulty.list)
    api(projects.features.difficulty.values)
    api(projects.features.tags.list)
    api(projects.features.tags.songs)
    api(projects.features.tags.values)
    api(projects.features.updates)
    api(projects.features.viewer)
}

android {
    namespace = "com.vgleadsheets.features.all"
}
