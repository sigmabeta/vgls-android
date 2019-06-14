package com.vgleadsheets.args

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SongArgs(val songId: Long) : Parcelable