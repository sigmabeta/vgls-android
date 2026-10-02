package com.vgleadsheets.model

data class Composer(
    val id: Long,
    val name: String,
    val songs: List<Song>?,
    val songCount: Int,
    val photoUrl: String?,
    val hasVocalSongs: Boolean,
    val sheetsPlayed: Int,
    val isFavorite: Boolean,
    val isAvailableOffline: Boolean,
    /**
     * Cover images of the games this composer is credited on, the ones with the most of their songs
     * first; the fallback for [photoUrl], which VGLS doesn't populate. Empty if none of their games
     * has art (or the source didn't load it).
     */
    val gameImageUrls: List<String> = emptyList(),
) {
    /** The images to show for the composer: their own [photoUrl] if they have one, else [gameImageUrls]. */
    val imageUrls: List<String>
        get() = photoUrl?.let(::listOf) ?: gameImageUrls
}
