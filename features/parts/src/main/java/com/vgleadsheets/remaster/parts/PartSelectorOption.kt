package com.vgleadsheets.remaster.parts

import com.vgleadsheets.strings.VglsStringId

enum class PartSelectorOption(
    val apiId: String,
    val midLengthResId: VglsStringId,
    val longResId: VglsStringId
) {
    C("C", VglsStringId.PART_MID_C, VglsStringId.PART_LONG_C),
    B("Bb", VglsStringId.PART_MID_B, VglsStringId.PART_LONG_B),
    E("Eb", VglsStringId.PART_MID_E, VglsStringId.PART_LONG_E),
    F("F", VglsStringId.PART_MID_F, VglsStringId.PART_LONG_F),
    G("G", VglsStringId.PART_MID_G, VglsStringId.PART_LONG_G),
    ALTO("Alto", VglsStringId.PART_MID_ALTO, VglsStringId.PART_LONG_ALTO),
    BASS("Bass", VglsStringId.PART_MID_BASS, VglsStringId.PART_LONG_BASS),
    VOCAL("Vocals", VglsStringId.PART_MID_VOCAL, VglsStringId.PART_LONG_VOCAL),
}
