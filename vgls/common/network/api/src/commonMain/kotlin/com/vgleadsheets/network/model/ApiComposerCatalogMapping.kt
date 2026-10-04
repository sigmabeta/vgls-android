package com.vgleadsheets.network.model

import kotlinx.serialization.Serializable

/**
 * vgm-metadata's VGLS composer catalog: [entries] maps VGLS composer ids to their Wikidata match
 * (photo and credit included); [unmatched] and [pending] list the ids with no match or none yet.
 */
@Serializable
data class ApiComposerCatalogMapping(
    val entries: Map<String, ApiComposerCatalogEntry> = emptyMap(),
    val unmatched: List<String> = emptyList(),
    val pending: List<String> = emptyList(),
)

/** One composer's Wikidata [match], and whether it came from a manual `override` or the `cache`. */
@Serializable
data class ApiComposerCatalogEntry(
    val source: String,
    val match: ApiComposerMatch,
)

/**
 * A Wikidata person: their [id] (a QID), [name], and [imageUrl] with the credit its license
 * requires when the server stored a photo. The names mirror the server `Resolution` fields; the
 * credit fields are null for a match with no photo.
 */
@Serializable
data class ApiComposerMatch(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val imageAuthor: String? = null,
    val imageLicense: String? = null,
    val imageLicenseUrl: String? = null,
    val imageSourceUrl: String? = null,
)
