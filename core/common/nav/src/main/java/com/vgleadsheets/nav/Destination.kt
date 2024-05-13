package com.vgleadsheets.nav

enum class Destination(
    val destName: String,
    val argType: ArgType,
    val renderAsGrid: Boolean = false,
) {
    HOME("home", ArgType.NONE),
    BROWSE("browse", ArgType.NONE),
    GAME_DETAIL("games", ArgType.LONG),
    GAMES_LIST("games", ArgType.NONE, renderAsGrid = true),
    COMPOSER_DETAIL("composers", ArgType.LONG),
    COMPOSERS_LIST("composers", ArgType.NONE, renderAsGrid = true),
    SONG_VIEWER("songs/viewer", ArgType.LONG),
    SONG_DETAIL("songs", ArgType.LONG),
    SONGS_LIST("songs", ArgType.NONE),
    TAGS_LIST("tags", ArgType.NONE),
    FAVORITES("favorites", ArgType.NONE),
    ;

    fun forId(id: Long) = "$destName/$id"
    fun forString(arg: String) = "$destName/$arg"
    fun template(): String {
        return if (argType == ArgType.NONE) {
            destName
        } else {
            "$destName/{$ARG_TEMPLATE}"
        }
    }
}

const val ARG_TEMPLATE = "replaceme"
