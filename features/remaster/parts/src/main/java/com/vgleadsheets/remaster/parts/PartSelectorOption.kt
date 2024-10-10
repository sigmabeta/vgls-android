package com.vgleadsheets.remaster.parts

import com.vgleadsheets.ui.StringId

enum class PartSelectorOption(
    val apiId: String,
    val midLengthResId: StringId,
    val longResId: StringId
) {
    C("C", StringId.PART_MID_C, StringId.PART_LONG_C),
    B("Bb", StringId.PART_MID_B, StringId.PART_LONG_B),
    E("Eb", StringId.PART_MID_E, StringId.PART_LONG_E),
    F("F", StringId.PART_MID_F, StringId.PART_LONG_F),
    G("G", StringId.PART_MID_G, StringId.PART_LONG_G),
    ALTO("Alto", StringId.PART_MID_ALTO, StringId.PART_LONG_ALTO),
    BASS("Bass", StringId.PART_MID_BASS, StringId.PART_LONG_BASS),
    VOCAL("Vocals", StringId.PART_MID_VOCAL, StringId.PART_LONG_VOCAL),
}
