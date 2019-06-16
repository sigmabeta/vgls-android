package com.vgleadsheets.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vgleadsheets.model.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@Suppress("TooManyFunctions")
abstract class BaseArrayAdapter<T : ListItem<T>, VH : BaseViewHolder<*, *, *>>(val view: ListView) :
    RecyclerView.Adapter<VH>() {
    protected var datasetInternal: List<T>? = null

    protected var diffStartTime = 0L

    var dataset: List<T>?
        get() {
            return null
        }

        set(value) {
            diffStartTime = System.currentTimeMillis()
            if (value === datasetInternal) {
                Timber.i("Received existing list of size %d", value?.size ?: -1)
            } else {
                if (datasetInternal == null && value != null) {
                    showFromEmptyList(value)
                } else {
                    startAsyncListRefresh(value)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val item = LayoutInflater.from(parent.context)?.inflate(viewType, parent, false)

        return if (item != null) {
            createViewHolder(item, viewType)
        } else {
            throw IllegalArgumentException("Unable to inflate view of type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        getItem(position)?.let {
            bind(holder, it)
        } ?: let {
            Timber.e("Can't bind view; dataset is not valid.")
        }
    }

    override fun getItemCount() = datasetInternal?.size ?: 0

    override fun getItemId(position: Int) = position.toLong()

    open fun getItem(position: Int) = datasetInternal?.get(position)

    override fun getItemViewType(position: Int) = getLayoutId(getItem(position)) ?: -1

    fun onItemClick(position: Int) {
        view.onItemClick(position)
    }

    abstract fun createViewHolder(view: View, viewType: Int): VH

    protected abstract fun bind(holder: VH, item: T)

    @LayoutRes
    protected abstract fun getLayoutId(item: T?): Int?

    protected open fun showFromEmptyList(value: List<T>) {
        Timber.i("Animating in all items in list of size %d", value.size)
        datasetInternal = value
        notifyDataSetChanged()
    }

    private fun printBenchmark() {
        if (diffStartTime > 0) {
            val timeDiff = System.currentTimeMillis() - diffStartTime
            Timber.i("Benchmark: %s after %d ms.", "Diff Complete", timeDiff)
        }
    }

    private fun startAsyncListRefresh(input: List<T>?) {
        Timber.i("Executing DiffUtil on list of size %d", input?.size ?: -1)

        Observable.defer<DiffUtil.DiffResult> {
            val callback = DiffCallback(datasetInternal, input)
            val result = DiffUtil.calculateDiff(callback)

            return@defer Observable.just(result)
        }

                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            printBenchmark()
                            datasetInternal = input
                            it.dispatchUpdatesTo(this@BaseArrayAdapter)
                        },
                        {
                            Timber.e("Error in DiffUtils")
                        }
)
    }
}
