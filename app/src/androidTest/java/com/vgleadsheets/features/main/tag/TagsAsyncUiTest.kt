package com.vgleadsheets.features.main.tag

import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.tag.value.song.tagValueSongList
import com.vgleadsheets.features.main.tag.value.tagValueList
import com.vgleadsheets.features.main.viewer.viewer
import org.junit.Test

class TagsAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_TAG

    @Test
    fun arbitraryGameClickingArbitrarySongShowsViewerScreen() {
        tagList(this) {
            checkFirstTagIs(TAG_KEY_FIRST_TITLE, TAG_KEY_FIRST_SUBTITLE)
            clickTagWithTitle(TAG_KEY_ARBITRARY_TITLE, TAG_KEY_ARBITRARY_SCROLL_POS)
        }

        tagValueList(this, TAG_KEY_ARBITRARY_TITLE, TAG_KEY_ARBITRARY_OPTION_COUNT) {
            checkFirstTagValueIs(
                TAG_KEY_ARBITRARY_VALUE_FIRST_TITLE,
                TAG_KEY_ARBITRARY_VALUE_FIRST_SUBTITLE
            )
            clickTagWithTitle(
                TAG_KEY_ARBITRARY_VALUE_ARBITRARY_TITLE,
                TAG_KEY_ARBITRARY_VALUE_ARBITRARY_SCROLL_POS
            )
        }

        tagValueSongList(
            this,
            "$TAG_KEY_ARBITRARY_TITLE: $TAG_KEY_ARBITRARY_VALUE_ARBITRARY_TITLE",
            TAG_KEY_ARBITRARY_VALUE_ARBITRARY_SHEET_COUNT
        ) {
            checkFirstSongIs(TAG_KEY_ARBITRARY_VALUE_ARBITRARY_SONG_FIRST_TITLE)
            checkFirstSongGameIs(TAG_KEY_ARBITRARY_VALUE_ARBITRARY_SONG_FIRST_SUBTITLE)
            clickSheetWithTitle(
                TAG_KEY_ARBITRARY_VALUE_ARBITRARY_SONG_ARBITRARY_TITLE,
                TAG_KEY_ARBITRARY_VALUE_ARBITRARY_SONG_ARBITRARY_SCROLL_POS
            )
        }

        viewer(this) {
            checkPageVisible(1)
        }
    }

    companion object {
        const val TAG_KEY_FIRST_TITLE = "Adipiscing Tincidunt Justo Hendrerit"
        const val TAG_KEY_FIRST_SUBTITLE = "Consectetur, Dictum Ultricies Quis and 7 more…"

        const val TAG_KEY_ARBITRARY_TITLE = "Metus"
        const val TAG_KEY_ARBITRARY_OPTION_COUNT = "9 Options"
        const val TAG_KEY_ARBITRARY_SCROLL_POS = 12

        const val TAG_KEY_ARBITRARY_VALUE_FIRST_TITLE = "Dictum Aliquam"
        const val TAG_KEY_ARBITRARY_VALUE_FIRST_SUBTITLE = "Curabitur Dolor Finibus and 4 more…"
        const val TAG_KEY_ARBITRARY_VALUE_ARBITRARY_TITLE = "Pellentesque Eget"
        const val TAG_KEY_ARBITRARY_VALUE_ARBITRARY_SHEET_COUNT = "6 Sheets"
        const val TAG_KEY_ARBITRARY_VALUE_ARBITRARY_SCROLL_POS = 6

        const val TAG_KEY_ARBITRARY_VALUE_ARBITRARY_SONG_FIRST_TITLE = "Ante Curabitur"
        const val TAG_KEY_ARBITRARY_VALUE_ARBITRARY_SONG_FIRST_SUBTITLE = "Velit Eget"
        const val TAG_KEY_ARBITRARY_VALUE_ARBITRARY_SONG_ARBITRARY_TITLE = "Metus Velit"
        const val TAG_KEY_ARBITRARY_VALUE_ARBITRARY_SONG_ARBITRARY_SCROLL_POS = 5
    }
}
