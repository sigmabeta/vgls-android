package com.vgleadsheets.ui

import com.vgleadsheets.ui.strings.R

fun StringId.id(): Int {
    return when (this) {
        StringId.APP_NAME -> R.string.app_name
        StringId.SCREEN_TITLE_BROWSE -> R.string.screen_title_browse
        StringId.SCREEN_TITLE_BROWSE_GAMES -> R.string.screen_title_games
        StringId.SCREEN_TITLE_BROWSE_COMPOSERS -> R.string.screen_title_composers
        StringId.SCREEN_TITLE_BROWSE_TAGS -> R.string.screen_title_tags
        StringId.SCREEN_TITLE_BROWSE_ALL -> R.string.screen_title_all
        StringId.SCREEN_TITLE_BROWSE_FAVORITES -> R.string.screen_title_favorites
        StringId.SCREEN_TITLE_SEARCH -> R.string.screen_title_search

        StringId.SCREEN_SUBTITLE_SONG_DETAIL -> R.string.screen_subtitle_song_detail

        StringId.SECTION_HEADER_COMPOSERS_FROM_SONG -> R.string.section_header_composers_from_song
        StringId.SECTION_HEADER_COMPOSERS_FROM_GAME -> R.string.section_header_composers_from_game
        StringId.SECTION_HEADER_SONGS_FROM_GAME -> R.string.section_header_songs_from_game
        StringId.SECTION_HEADER_SONGS_FROM_COMPOSER -> R.string.section_header_songs_from_composer
        StringId.SECTION_HEADER_GAMES_FROM_SONG -> R.string.section_header_games_from_song
        StringId.SECTION_HEADER_GAMES_FROM_COMPOSER -> R.string.section_header_games_from_composer
        StringId.SECTION_HEADER_DIFFICULTY_FOR_SONG -> R.string.section_header_difficulty_for_song
        StringId.SECTION_HEADER_ABOUT_SONG -> R.string.section_header_about_song

        StringId.LABEL_SONG_ALSO_KNOWN_AS -> R.string.label_song_also_known_as
    }
}
