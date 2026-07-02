package com.vgleadsheets.model.updates

@Suppress("MaxLineLength")
data class AppUpdate(
    val versionCode: Int,
    val versionName: String,
    val releaseDate: String,
    val changes: List<String>,
) {
    companion object {
        val VERSION_2_0_0 = AppUpdate(
            versionCode = 20000,
            versionName = "2.0.0",
            releaseDate = "October 23, 2024",
            changes = listOf(
                "Full redesign of the entire app UI.",
                "App now uses Jetpack Compose toolkit (don't worry if you don't know what this means.)",
                "Sheets are now downloaded as PDFs, which is faster and takes less space on device storage. This should also result in sharper sheets at different sizes.",
                "UI has been optimized for large screens: tablets, foldables, etc.",
                "Numerous performance optimizations to make screens load faster.",
                "Home screen added, which uses your sheet-reading history (saved on device only) to make some basic recommendations.",
                "Major improvements to the search feature, which was basically broken before.",
                "Basically a full rewrite of all functionality. In many cases, this directly makes things better in ways you don't care to read here, but more importantly, new features should be easier to add.",
            )
        )

        val VERSION_2_0_3 = AppUpdate(
            versionCode = 20003,
            versionName = "2.0.3",
            releaseDate = "November 7, 2024",
            changes = listOf(
                "Fix \"Keep screen on\" setting, which previously did nothing. Oops.",
                "Add shortcut to Favorites on wide screens."
            )
        )

        val VERSION_2_0_4 = AppUpdate(
            versionCode = 20004,
            versionName = "2.0.4",
            releaseDate = "April 8, 2025",
            changes = listOf(
                "Add button that manually checks for updates to the settings screen.",
                "Add button that clears usage history to the settings screen.",
                "Add button that clears sheet database to the settings screen.",
                "Wrote a privacy policy, accessible through the settings screen."
            )
        )

        val VERSION_2_0_5 = AppUpdate(
            versionCode = 20005,
            versionName = "2.0.5",
            releaseDate = "July 2, 2025",
            changes = listOf(
                "Sheets are now zoomable. Because they're rendered as PDFs, zooming in renders more detail.",
                "Fix multiple crashes and minor bugs."
            )
        )

        val VERSION_2_1_0 = AppUpdate(
            versionCode = 21000,
            versionName = "2.1.0",
            releaseDate = "April 28, 2026",
            changes = listOf(
                "Page-turn pedals now work with the Viewer screen.",
                "Sheets can now be marked as \"available offline\". The app will download all PDFs for that sheet.",
                "\"Available offline\" sheets will be downloaded in the background, once a week, when plugged in and on wifi. This process will also check for new revisions of these sheets.",
                "Games and composers can also be marked for offline availability; the app will offline all sheets associated with that game or composer.",
                "Added a new screen that shows all content made available offline.",
                "Added more descriptive error messages when in bad network conditions.",
            )
        )
    }
}
