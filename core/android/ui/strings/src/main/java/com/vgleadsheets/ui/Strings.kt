package com.vgleadsheets.ui

import com.vgleadsheets.ui.strings.R

fun StringId.id(): Int {
    return when (this) {
        StringId.APP_NAME -> R.string.app_name
        StringId.SCREEN_TITLE_PART_SELECTOR -> R.string.screen_title_parts
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

        StringId.BROWSE_LINK_FAVORITES -> R.string.browse_link_favorites
        StringId.BROWSE_LINK_GAME -> R.string.browse_link_game
        StringId.BROWSE_LINK_COMPOSER -> R.string.browse_link_composer
        StringId.BROWSE_LINK_TAG -> R.string.browse_link_tag
        StringId.BROWSE_LINK_SHEETS -> R.string.browse_link_sheets

        StringId.PART_MID_C -> R.string.part_mid_c
        StringId.PART_MID_B -> R.string.part_mid_b
        StringId.PART_MID_E -> R.string.part_mid_e
        StringId.PART_MID_F -> R.string.part_mid_f
        StringId.PART_MID_G -> R.string.part_mid_g
        StringId.PART_MID_ALTO -> R.string.part_mid_alto
        StringId.PART_MID_BASS -> R.string.part_mid_bass
        StringId.PART_MID_VOCAL -> R.string.part_mid_vocal

        StringId.PART_LONG_C -> R.string.part_long_c
        StringId.PART_LONG_B -> R.string.part_long_b
        StringId.PART_LONG_E -> R.string.part_long_e
        StringId.PART_LONG_F -> R.string.part_long_f
        StringId.PART_LONG_G -> R.string.part_long_g
        StringId.PART_LONG_ALTO -> R.string.part_long_alto
        StringId.PART_LONG_BASS -> R.string.part_long_bass
        StringId.PART_LONG_VOCAL -> R.string.part_long_vocal
    }
}
