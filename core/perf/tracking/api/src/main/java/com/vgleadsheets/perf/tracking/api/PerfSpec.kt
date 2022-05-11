package com.vgleadsheets.perf.tracking.api

enum class PerfSpec(val completionTargetTime: Long) {
    ABOUT(TARGET_COMPLETION_NONE_MS),
    COMPOSER(1000L),
    COMPOSERS(TARGET_COMPLETION_LIST_MS),
    DEBUG(TARGET_COMPLETION_NONE_MS),
    GAME(1000L),
    GAMES(TARGET_COMPLETION_LIST_MS),
    HUD(TARGET_COMPLETION_NONE_MS),
    JAM(500L),
    JAMS(1000L),
    LICENSE(-1L),
    SEARCH(-1L),
    SETTINGS(500L),
    SHEET(500L),
    SONGS(TARGET_COMPLETION_LIST_MS),
    TAG_KEY(500L),
    TAG_SONGS(2000L),
    TAG_VALUE(TARGET_COMPLETION_LIST_MS),
    VIEWER(1000L);

    companion object {
        private const val MS_PER_FRAME = 16.666667f

        private const val TARGET_VIEW_CREATED_FRAMES = 4
        private const val TARGET_TITLE_LOADED_FRAMES = 6
        private const val TARGET_TRANSITION_START_FRAMES = 20
        private const val TARGET_PARTIAL_LOAD_FRAMES = 90

        private const val TARGET_VIEW_CREATED_MS =
            (MS_PER_FRAME * TARGET_VIEW_CREATED_FRAMES).toLong()
        private const val TARGET_TITLE_LOADED_MS =
            (MS_PER_FRAME * TARGET_TITLE_LOADED_FRAMES).toLong()
        private const val TARGET_TRANSITION_START_MS =
            (MS_PER_FRAME * TARGET_TRANSITION_START_FRAMES).toLong()
        private const val TARGET_PARTIAL_LOAD_MS =
            (MS_PER_FRAME * TARGET_PARTIAL_LOAD_FRAMES).toLong()
    }
}

private const val TARGET_COMPLETION_NONE_MS = -1L
private const val TARGET_COMPLETION_LIST_MS = 3000L
