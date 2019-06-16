package com.vgleadsheets.songs

import android.view.View
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.list_item_sheet.view.*

class SongViewHolder(view: View, adapter: SongListAdapter) :
    BaseViewHolder<Song, SongViewHolder, SongListAdapter>(view, adapter) {
    override fun bind(toBind: Song) {
        view.text_sheet_name.text = toBind.name
        view.text_sheet_count.text = "${toBind.pageCount} pages"
    }
}
