package com.vgleadsheets.search

import android.view.View
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.search.SearchResultType
import com.vgleadsheets.recyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.list_item_search_result.view.*

class SearchResultViewHolder(view: View, adapter: SearchResultAdapter) :
    BaseViewHolder<SearchResult, SearchResultViewHolder, SearchResultAdapter>(view, adapter) {
    override fun bind(toBind: SearchResult) {
        val stringId = when (toBind.type) {
            SearchResultType.GAME -> R.string.label_type_game
            SearchResultType.COMPOSER -> R.string.label_type_composer
            SearchResultType.SONG -> R.string.label_type_song
        }
        view.text_result_name.text = toBind.name
        view.text_result_type.text = view.resources.getString(stringId)
    }
}