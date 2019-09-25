package com.vgleadsheets.features.main.viewer.pages

import android.view.View
import com.squareup.picasso.Callback
import com.vgleadsheets.animation.fadeInFromZero
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.loadImageFull
import com.vgleadsheets.model.pages.Page
import com.vgleadsheets.recyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.list_item_page.view.*
import timber.log.Timber

class PageViewHolder(view: View, adapter: PageAdapter) :
    BaseViewHolder<Page, PageViewHolder, PageAdapter>(view, adapter) {
    override fun bind(toBind: Page) {
        showLoading()
        view.image_sheet.loadImageFull(toBind.imageUrl, object : Callback {
            override fun onSuccess() {
                hideLoading()
            }

            override fun onError(e: Exception?) {
                Timber.e("Failed to load image: ${e?.message}")
                hideLoading()
                adapter.onError(adapterPosition)
            }
        })
    }

    private fun showLoading() {
        view.progress_loading.fadeInFromZero()
    }

    private fun hideLoading() {
        view.progress_loading.fadeOutGone()
    }
}
