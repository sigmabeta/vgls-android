package com.vgleadsheets.args

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NullableStringArgs(val input: String?) : Parcelable
