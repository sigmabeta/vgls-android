package com.vgleadsheets.features.main.songs

import android.view.View
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.list_item_sheet.view.*

class SongViewHolder(view: View, adapter: SongListAdapter) :
    BaseViewHolder<Song, SongViewHolder, SongListAdapter>(view, adapter) {
    override fun bind(toBind: Song) {
        view.text_sheet_composer.text = when (toBind.composers?.size) {
            null, 0 -> "Unknown Composer"
            1 -> toBind.composers?.get(0)?.name ?: "Unknown Composer"
            else -> "Various Composers"
        }

        view.text_sheet_name.text = toBind.name
    }
}
