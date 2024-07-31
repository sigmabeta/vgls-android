package com.vgleadsheets.ui

import com.vgleadsheets.ui.icons.R

fun Icon.id(): Int {
    return when (this) {
        Icon.ALBUM -> R.drawable.ic_album_24dp
        Icon.DESCRIPTION -> R.drawable.ic_description_24dp
        Icon.JAM_EMPTY -> R.drawable.ic_jam_unfilled
        Icon.JAM_FILLED -> R.drawable.ic_jam_filled
        Icon.PERSON -> R.drawable.ic_person_24dp
        Icon.REFRESH -> R.drawable.ic_refresh_24dp
        Icon.TAG -> R.drawable.ic_tag_black_24dp
    }
}
