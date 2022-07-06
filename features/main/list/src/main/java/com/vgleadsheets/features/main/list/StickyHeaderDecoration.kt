package com.vgleadsheets.features.main.list

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import com.vgleadsheets.components.ComponentViewHolder
import com.vgleadsheets.components.LoadingTitleListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.recyclerview.ComponentAdapter

class StickyHeaderDecoration(
    private val adapter: ComponentAdapter,
    root: View
) : ItemDecoration() {
    private val headerViewHolder by lazy {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(root.context),
            R.layout.list_component_title,
            null,
            false
        )

        binding.root.visibility = VISIBLE

        ComponentViewHolder(
            binding
        )
    }

    private val stickyHeader: View
        get() = headerViewHolder.itemView

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: State) {
        super.onDrawOver(canvas, parent, state)

        val scrollingHeader = parent.getChildAt(0)

        val headerModel = adapter.getFirstItem()
        if ((headerModel !is TitleListModel) && (headerModel !is LoadingTitleListModel)) {
            return
        }

        // Don't render if loading views are at the top.
        if (scrollingHeader.id == com.vgleadsheets.ui_core.R.id.component_loading_name_caption) {
            return
        }

        // Check if the title view is at the top; if not, render the smallest version of the header.
        val progress = if (scrollingHeader.id == com.vgleadsheets.ui_core.R.id.root_title) {
            1.0f - minOf(scrollingHeader.bottom / scrollingHeader.height.toFloat(), 1.0f)
        } else {
            1.0f
        }

        val modelDupe = (headerModel as TitleListModel).copy(
            shrinkPercent = progress,
            hidden = progress <= 0.0f,
            shouldSetMinHeightOnly = true
        )
        headerViewHolder.bind(modelDupe)

        layoutHeaderView(scrollingHeader)
        canvas.drawHeaderView()
    }

    private fun layoutHeaderView(scrollingView: View) {
        stickyHeader.measure(
            MeasureSpec.makeMeasureSpec(scrollingView.width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        )
        stickyHeader.layout(scrollingView.left, 0, scrollingView.right, stickyHeader.measuredHeight)
    }

    private fun Canvas.drawHeaderView() {
        save()
        stickyHeader.draw(this)
        restore()
    }
}
