package com.vgleadsheets.features.main.hud.parts

import android.view.View
import androidx.core.content.ContextCompat
import com.vgleadsheets.recyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.list_item_part.view.*

class PartViewHolder(view: View, adapter: PartsAdapter) :
        BaseViewHolder<PartSelectorItem, PartViewHolder, PartsAdapter>(view, adapter) {
    override fun bind(toBind: PartSelectorItem) {
        view.text_part_name.setText(toBind.resId)

        val color = if (toBind.selected) {
            ContextCompat.getColor(view.context, android.R.color.holo_red_dark)
        } else {
            ContextCompat.getColor(view.context, android.R.color.transparent)
        }

        view.setBackgroundColor(color)
    }
}