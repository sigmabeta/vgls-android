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
     * first; the fallback when no photo is available. Empty if none of their games has art (or the
     * source didn't load it).
     */
    val gameImageUrls: List<String> = emptyList(),
    /** The composer's photo from vgm-metadata (Wikimedia Commons), or null if they have none. */
    val metadataPhotoUrl: String? = null,
    /** The photo's author, when its license requires credit. */
    val metadataPhotoAuthor: String? = null,
    /** The photo's license name, e.g. `CC BY-SA 4.0`. */
    val metadataPhotoLicense: String? = null,
    /** A link to the photo's license text. */
    val metadataPhotoLicenseUrl: String? = null,
    /** The photo's page at its source, to link for attribution. */
    val metadataPhotoSourceUrl: String? = null,
) {
    /**
     * The images to show for the composer: their vgm-metadata photo if they have one, else VGLS's
     * own [photoUrl], else [gameImageUrls].
     */
    val imageUrls: List<String>
        get() = metadataPhotoUrl?.let(::listOf) ?: photoUrl?.let(::listOf) ?: gameImageUrls
}
