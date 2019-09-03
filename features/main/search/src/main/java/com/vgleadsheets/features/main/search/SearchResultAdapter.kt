package com.vgleadsheets.features.main.search

import android.view.View
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.search.SearchResultType
import com.vgleadsheets.recyclerview.BaseArrayAdapter

class SearchResultAdapter(val view: SearchFragment) :
    BaseArrayAdapter<SearchResult, SearchResultViewHolder>() {
    override fun createViewHolder(view: View, viewType: Int) = SearchResultViewHolder(view, this)

    override fun bind(holder: SearchResultViewHolder, item: SearchResult) = holder.bind(item)

    override fun getLayoutId(item: SearchResult?) = R.layout.list_item_search_result

    override fun onItemClick(position: Int) {
        val id = dataset!![position].id
        when (dataset!![position].type) {
            SearchResultType.GAME -> view.onGameClicked(id)
            SearchResultType.SONG -> view.onSongClicked(id)
            SearchResultType.COMPOSER -> view.onComposerClicked(id)
        }
    }
}
