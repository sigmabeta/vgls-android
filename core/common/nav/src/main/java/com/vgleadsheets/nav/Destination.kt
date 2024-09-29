package com.vgleadsheets.nav

enum class Destination(
    val destName: String,
    val argType: ArgType,
    val isImplemented: Boolean = true,
) {
    HOME("home", ArgType.NONE),
    BROWSE("browse", ArgType.NONE),
    SEARCH("search", ArgType.NONE),
    MENU("menu", ArgType.NONE),
    PART_PICKER("parts", ArgType.NONE),
    GAME_DETAIL("games", ArgType.LONG),
    GAMES_LIST("games", ArgType.NONE),
    COMPOSER_DETAIL("composers", ArgType.LONG),
    COMPOSERS_LIST("composers", ArgType.NONE),
    SONG_VIEWER("songs/viewer", ArgType.TWO),
    SONG_DETAIL("songs", ArgType.LONG),
    SONGS_LIST("songs", ArgType.NONE),
    DIFFICULTY_LIST("difficulties", ArgType.NONE),
    DIFFICULTY_VALUES_LIST("difficulties", ArgType.LONG),
    TAGS_LIST("tags", ArgType.NONE),
    TAGS_VALUES_LIST("tags", ArgType.LONG),
    TAGS_VALUES_SONG_LIST("tags/value", ArgType.LONG),
    FAVORITES("favorites", ArgType.NONE),
    LICENSES("licenses", ArgType.NONE),
    ;

    fun noArgs() = destName
    fun forId(id: Long) = "$destName/$id"
    fun forTwoArgs(id: Long, second: Long) = "$destName/$id/$second"
    fun forString(arg: String) = "$destName/$arg"
    fun template(): String {
        return when (argType) {
            ArgType.NONE -> destName
            ArgType.TWO -> "$destName/{$ARG_TEMPLATE_ONE}/{$ARG_TEMPLATE_TWO}"
            else -> "$destName/{$ARG_TEMPLATE_ONE}"
        }
    }
}

const val ARG_TEMPLATE_ONE = "arg_one"
const val ARG_TEMPLATE_TWO = "arg_two"
