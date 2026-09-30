package com.vgleadsheets.model

data class Game(
    val id: Long,
    val name: String,
    val songs: List<Song>?,
    val hasVocalSongs: Boolean,
    val songCount: Int,
    val photoUrl: String?,
    val sheetsPlayed: Int,
    val isFavorite: Boolean,
    val isAvailableOffline: Boolean,
    /**
     * The IGDB image id of the game's cover art (e.g. `co3plw`), from the vgm-metadata server's VGLS
     * catalog; null if the game has no IGDB match or its match has no cover.
     */
    val igdbImageId: String? = null,
) {
    /** The image to show for the game: VGLS's own [photoUrl] if it has one, else its IGDB cover. */
    val imageUrl: String?
        get() = photoUrl ?: igdbImageId?.let(IgdbImages::coverUrl)
}
