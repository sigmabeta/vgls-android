package com.vgleadsheets.args

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ViewerArgs(
    val jamId: Long? = null,
    val songId: Long? = null
) : Parcelable
