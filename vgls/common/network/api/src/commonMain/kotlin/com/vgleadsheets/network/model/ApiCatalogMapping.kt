package com.vgleadsheets.network.model

import kotlinx.serialization.Serializable

/**
 * vgm-metadata's VGLS catalog: [entries] maps VGLS game ids to their IGDB match; [unmatched] and
 * [pending] list the ids with no match or none yet.
 */
@Serializable
data class ApiCatalogMapping(
    val entries: Map<String, ApiCatalogEntry> = emptyMap(),
    val unmatched: List<String> = emptyList(),
    val pending: List<String> = emptyList(),
)

/** One game's IGDB [match], and whether it came from a manual `override` or the `cache`. */
@Serializable
data class ApiCatalogEntry(
    val source: String,
    val match: ApiIgdbMatch,
)

/** An IGDB game: its [id], [name], and cover [imageUrl] if it has one. */
@Serializable
data class ApiIgdbMatch(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
)
