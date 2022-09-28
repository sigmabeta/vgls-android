package com.vgleadsheets.args

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ViewerArgs(
    val songId: Long? = null,
    val jamId: Long? = null
) : Parcelable
