package com.vgleadsheets.nav

enum class Destination(
    private val destName: String,
) {
    HOME("home"),
    BROWSE("browse"),
    GAMES_LIST("games"),
    GAME_DETAIL("games" + ARG),
    COMPOSERS_LIST("composers"),
    COMPOSER_DETAIL("composers" + ARG),
    SONGS_LIST("songs"),
    SONG_DETAIL("songs" + ARG),
    SONG_VIEWER("songs/viewer" + ARG),
    ;

    fun noArgs() = destName
    fun idTemplate() = destName.replace(ARG, "/{$ARG_DEST_ID}")
    fun forId(id: Long) = destName.replace(ARG, "/$id" )

}

private val ARG = "/replaceme"
val ARG_DEST_ID = "destId"
