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
    api(projects.features.home.real)
    api(projects.features.browse.real)
    api(projects.features.search.real)
    api(projects.features.games.list.real)
    api(projects.features.games.detail.real)
    api(projects.features.composers.list.real)
    api(projects.features.composers.detail.real)
    api(projects.features.favorites.real)
    api(projects.features.menu.real)
    api(projects.features.offline.content.real)
    api(projects.features.offline.updates.real)
    api(projects.features.parts.real)
    api(projects.features.songs.list.real)
    api(projects.features.songs.detail.real)
    api(projects.features.difficulty.list.real)
    api(projects.features.difficulty.values.real)
    api(projects.features.tags.list.real)
    api(projects.features.tags.songs.real)
    api(projects.features.tags.values.real)
    api(projects.features.updates.real)
    api(projects.features.viewer.real)
}

android {
    namespace = "com.vgleadsheets.features.all"
}
