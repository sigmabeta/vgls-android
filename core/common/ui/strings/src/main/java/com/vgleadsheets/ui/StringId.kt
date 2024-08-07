package com.vgleadsheets.ui

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class StringId {
    APP_NAME,

    SCREEN_TITLE_PART_SELECTOR,
    SCREEN_TITLE_BROWSE,
    SCREEN_TITLE_BROWSE_GAMES,
    SCREEN_TITLE_BROWSE_COMPOSERS,
    SCREEN_TITLE_BROWSE_TAGS,
    SCREEN_TITLE_BROWSE_BY_TAG,
    SCREEN_TITLE_BROWSE_SONGS_WITH_TAG,
    SCREEN_TITLE_BROWSE_ALL,
    SCREEN_TITLE_BROWSE_FAVORITES,
    SCREEN_TITLE_SEARCH,
    SCREEN_TITLE_SETTINGS,

    SCREEN_SUBTITLE_SONG_DETAIL,

    SECTION_HEADER_SEARCH_SONGS,
    SECTION_HEADER_SEARCH_GAMES,
    SECTION_HEADER_SEARCH_COMPOSERS,
    SECTION_HEADER_COMPOSERS_FROM_SONG,
    SECTION_HEADER_COMPOSERS_FROM_GAME,
    SECTION_HEADER_SONGS_FROM_GAME,
    SECTION_HEADER_SONGS_FROM_COMPOSER,
    SECTION_HEADER_GAMES_FROM_SONG,
    SECTION_HEADER_GAMES_FROM_COMPOSER,
    SECTION_HEADER_DIFFICULTY_FOR_SONG,
    SECTION_HEADER_ABOUT_SONG,

    CTA_FAVORITE_ADD,
    CTA_FAVORITE_REMOVE,

    LABEL_SONG_ALSO_KNOWN_AS,

    HOME_SECTION_RECENT_SONGS,
    HOME_SECTION_MOST_SONGS_GAMES,
    HOME_SECTION_MOST_SONGS_COMPOSERS,
    HOME_SECTION_MOST_PLAYS_GAMES,
    HOME_SECTION_MOST_PLAYS_SONGS,
    HOME_SECTION_MOST_PLAYS_COMPOSERS,
    HOME_SECTION_RNG,

    HOME_ACTION_RANDOM_SONG,
    HOME_ACTION_RANDOM_GAME,
    HOME_ACTION_RANDOM_COMPOSER,

    BROWSE_LINK_FAVORITES,
    BROWSE_LINK_GAME,
    BROWSE_LINK_COMPOSER,
    BROWSE_LINK_TAG,
    BROWSE_LINK_SHEETS,

    PART_MID_C,
    PART_MID_B,
    PART_MID_E,
    PART_MID_F,
    PART_MID_G,
    PART_MID_ALTO,
    PART_MID_BASS,
    PART_MID_VOCAL,

    PART_LONG_C,
    PART_LONG_B,
    PART_LONG_E,
    PART_LONG_F,
    PART_LONG_G,
    PART_LONG_ALTO,
    PART_LONG_BASS,
    PART_LONG_VOCAL,

    SETTINGS_LABEL_KEEP_SCREEN_ON,
    SETTINGS_LABEL_LICENSES,
    SETTINGS_LABEL_WEBSITE,

    NOTIF_UPDATE_SUCCESS,

    ERROR_API_UPDATE,
    ERROR_DB_UPDATE,

    TAG_CAPTION_AND_OTHERS,
    TAG_CAPTION_SEPARATOR,
}
