package com.vgleadsheets.args

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ViewerArgs(
    val songId: Long? = null,
    val jamId: Long? = null
) : Parcelable
