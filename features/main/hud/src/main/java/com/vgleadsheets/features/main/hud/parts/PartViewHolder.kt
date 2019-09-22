package com.vgleadsheets.features.main.hud.parts

import android.view.View
import androidx.core.content.ContextCompat
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.recyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.list_item_part.view.*

class PartViewHolder(view: View, adapter: PartAdapter) :
        BaseViewHolder<PartSelectorItem, PartViewHolder, PartAdapter>(view, adapter) {
    override fun bind(toBind: PartSelectorItem) {
        view.text_part_name.setText(toBind.resId)
        view.text_part_name.setTextColor(
            if (toBind.selected) {
                ContextCompat.getColor(view.context, R.color.colorPrimary)
            } else {
                ContextCompat.getColor(view.context, R.color.hint_text)
            }
        )

        view.scaleX = if (toBind.selected) SCALE_SELECTED else SCALE_UNSELECTED
        view.scaleY = if (toBind.selected) SCALE_SELECTED else SCALE_UNSELECTED
    }

    companion object {
        const val SCALE_UNSELECTED = 0.8f
        const val SCALE_SELECTED = 1.0f
    }
}
