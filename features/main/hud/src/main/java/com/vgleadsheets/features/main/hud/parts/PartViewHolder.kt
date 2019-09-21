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

        view.scaleX = if (toBind.selected) 1.0f else 0.8f
        view.scaleY = if (toBind.selected) 1.0f else 0.8f
    }
}
