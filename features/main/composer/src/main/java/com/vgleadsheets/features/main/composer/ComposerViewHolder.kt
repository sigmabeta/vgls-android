package com.vgleadsheets.features.main.composer

import android.view.View
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.recyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.list_item_composer.view.*

class ComposerViewHolder(view: View, adapter: ComposerListAdapter) :
    BaseViewHolder<Composer, ComposerViewHolder, ComposerListAdapter>(view, adapter) {
    override fun bind(toBind: Composer) {
        view.text_composer_name.text = toBind.name
        view.text_sheet_count.text = "${toBind.songs?.size} sheets"
    }
}
