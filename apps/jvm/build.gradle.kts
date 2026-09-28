plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.metro)
    // Kotlin Compose compiler (shared with the Android UI) + JetBrains Compose desktop plugin
    // (provides compose.desktop.currentOs → the per-OS Skia native).
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

compose.desktop {
    application {
        mainClass = "com.vgleadsheets.jvm.MainKt"
    }
}

dependencies {
    // --- VGLS logic / data layer (all KMP now) ---
    implementation(projects.vgls.common.repository.real)
    implementation(projects.vgls.android.repository.di)
    implementation(projects.vgls.android.conversion.di)
    implementation(projects.vgls.android.database.di)
    implementation(projects.vgls.common.database.api)
    implementation(projects.vgls.common.network.api)
    implementation(projects.vgls.common.network.real)
    implementation(projects.vgls.common.network.fake)
    implementation(projects.vgls.common.downloader.api)
    implementation(projects.vgls.common.downloader.real)
    implementation(projects.vgls.common.downloader.fake)
    implementation(projects.vgls.common.offline.real)
    implementation(projects.vgls.common.environment.api)
    implementation(projects.vgls.common.urlinfo.real)
    implementation(projects.vgls.common.versions.real)
    implementation(projects.vgls.common.notif.real)
    implementation(projects.vgls.common.model.api)
    implementation(projects.vgls.common.appcomm.real)
    implementation(projects.vgls.common.settings.part.real)
    implementation(projects.vgls.common.wakelocks.api)
    implementation(projects.vgls.common.wakelocks.fake)

    // --- VGLS UI (KMP) ---
    implementation(projects.vgls.common.strings.api)
    implementation(projects.vgls.android.scaffold.real)
    implementation(projects.vgls.android.nav.real)
    implementation(projects.vgls.android.licenses.real)
    implementation(projects.vgls.android.viewmodel.real)
    implementation(projects.vgls.common.viewmodel.real)
    implementation(projects.vgls.android.ui.components.api)
    implementation(projects.vgls.android.ui.theme.api)
    implementation(projects.vgls.android.ui.list.api)
    implementation(projects.vgls.android.pdf.real)
    implementation(projects.vgls.android.images.real)
    // Coil core: the desktop app builds its own ImageLoader (PDF keyer/fetcher + the PDFBox decoder)
    // and registers it via SingletonImageLoader — the JVM analog of apps/android's ImagesModule.
    implementation(libs.coil.kt.core)

    // Fake (no-op) analytics.
    implementation(projects.vgls.common.analytics.fake)

    // --- Feature modules (enumerated so Metro aggregates their @ContributesIntoMap VMs) ---
    implementation(projects.features.browse.real)
    implementation(projects.features.composers.detail.real)
    implementation(projects.features.composers.list.real)
    implementation(projects.features.difficulty.list.real)
    implementation(projects.features.difficulty.values.real)
    implementation(projects.features.favorites.real)
    implementation(projects.features.games.detail.real)
    implementation(projects.features.games.list.real)
    implementation(projects.features.home.real)
    implementation(projects.features.menu.real)
    implementation(projects.features.offline.content.real)
    implementation(projects.features.offline.updates.real)
    implementation(projects.features.parts.real)
    implementation(projects.features.search.real)
    implementation(projects.features.songs.detail.real)
    implementation(projects.features.songs.list.real)
    implementation(projects.features.tags.list.real)
    implementation(projects.features.tags.songs.real)
    implementation(projects.features.tags.values.real)
    implementation(projects.features.updates.real)
    implementation(projects.features.viewer.real)
    implementation(projects.features.topbar.real)
    implementation(projects.features.navbar.real)

    // --- SAGE common (KMP) ---
    implementation(libs.sage.common.di)
    implementation(libs.sage.common.appinfo)
    implementation(libs.sage.common.time)
    implementation(libs.sage.common.events)
    implementation(libs.sage.common.debug)
    implementation(libs.sage.common.logging)
    implementation(libs.sage.common.analytics)
    implementation(libs.sage.common.appcomm)
    implementation(libs.sage.common.coroutines)
    implementation(libs.sage.common.connectivity)
    implementation(libs.sage.common.storage.common)
    implementation(libs.sage.common.ui.strings)
    implementation(libs.sage.common.ui.iconsApi)
    implementation(libs.sage.common.ui.iconsReal)
    implementation(libs.sage.common.ui.perfCompose)

    // --- Metro ViewModel plumbing ---
    implementation(libs.metrox.viewmodel)
    implementation(libs.metrox.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel)

    // --- Voyager navigation ---
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.transitions)

    // --- Ktor (VGLS API) + kotlinx ---
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.swing)
    implementation(libs.kotlinx.datetime)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.okio)

    // --- Room JVM driver ---
    implementation(libs.room.runtime)
    implementation(libs.sqlite.bundled)

    // --- Compose Multiplatform desktop ---
    implementation(libs.jetbrains.compose.runtime)
    implementation(libs.jetbrains.compose.foundation)
    implementation(libs.jetbrains.compose.material3)
    implementation(libs.jetbrains.compose.ui)
    implementation(compose.desktop.currentOs)
}
