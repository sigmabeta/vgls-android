package com.vgleadsheets.features.main.sheet

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.LabelRatingStarListModel
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.async.AsyncListState

data class SheetDetailState(
    val songId: Long,
    override val updateTime: Async<*> = Uninitialized,
    override val digest: Async<*> = Uninitialized,
    override val selectedPart: PartSelectorItem? = PartSelectorItem("C", R.string.part_c, true),
    override val listModels: List<ListModel> = emptyList(),
    override val data: SheetDetailData = SheetDetailData(),
    val clickedCtaModel: CtaListModel? = null,
    val clickedDetailModel: LabelValueListModel? = null,
    val clickedTagValueModel: LabelValueListModel? = null,
    val clickedRatingStarModel: LabelRatingStarListModel? = null
) : AsyncListState<SheetDetailData>(data = data) {
    constructor(idArgs: IdArgs) : this(idArgs.id)

    override fun updateListState(
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?,
        listModels: List<ListModel>,
        data: SheetDetailData
    ) = copy(
        updateTime = updateTime,
        digest = digest,
        selectedPart = selectedPart,
        listModels = listModels,
        data = data
    )
}
