package dev.steelahh.core

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/*
 * Author: steelahhh
 * 4/1/20
 */

open class BaseAdapter<T>(
    vararg delegates: AdapterDelegate<List<T>>
) : ListDelegationAdapter<List<T>>(*delegates) {

    private var diffDisposable: Disposable? = null

    protected open val diffItemCallback: DiffUtil.ItemCallback<T>? = null

    init {
        this.items = emptyList()
    }

    fun addTo(position: Int, item: T): Int {
        val pos = if (position <= items.size) {
            items = items.toMutableList().apply {
                add(position, item)
            }
            position
        } else {
            items = items + item
            items.lastIndex
        }
        notifyItemInserted(pos)
        return pos
    }

    fun removeItem(item: T): Int {
        val iof = items.indexOf(item)
        if (iof != -1) {
            items = items.toMutableList().apply {
                removeAt(iof)
            }
            notifyItemRemoved(iof)
        }

        return iof
    }

    fun removeItemByIndex(index: Int) {
        if (index < 0 || index > itemCount) return
        items = items.toMutableList().apply {
            removeAt(index)
        }
        notifyItemRemoved(index)
    }

    fun updateItem(position: Int, item: T, payload: Any? = null) {
        if (position >= itemCount) return
        val oldItems = items.toMutableList()
        oldItems[position] = item
        items = oldItems
        notifyItemChanged(position, payload)
    }

    fun getPositionByItem(item: T): Int {
        return items.indexOf(item)
    }

    fun copy(): List<T> = this.items.toList()

    fun replaceItems(
        items: List<T>,
        withDiff: Boolean = false,
        async: Boolean = false,
        detectMoves: Boolean = false
    ) {
        if (this.items === items) {
            return
        }
        val itemCallback = diffItemCallback
        if (withDiff && itemCallback != null) {
            val oldItems = this.items
            if (async) {
                replaceItemsAsync(oldItems, items, detectMoves, itemCallback)
            } else {
                replaceItemsSync(oldItems, items, detectMoves, itemCallback)
            }
        } else {
            justReplaceItems(items)
        }
    }

    private fun justReplaceItems(newItems: List<T>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }

    private fun replaceItemsAsync(
        oldItems: List<T>,
        newItems: List<T>,
        detectMoves: Boolean,
        diffUtil: DiffUtil.ItemCallback<T>
    ) {
        diffDisposable?.dispose()
        diffDisposable = Single
            .fromCallable { createDiff(oldItems, newItems, detectMoves, diffUtil) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { diffResult ->
                this.latchList(newItems, diffResult)
            }
    }

    private fun replaceItemsSync(
        oldItems: List<T>,
        newItems: List<T>,
        detectMoves: Boolean,
        itemCallback: DiffUtil.ItemCallback<T>
    ) = latchList(newItems, createDiff(oldItems, newItems, detectMoves, itemCallback))

    private fun createDiff(
        oldItems: List<T>,
        newItems: List<T>,
        detectMoves: Boolean,
        itemCallback: DiffUtil.ItemCallback<T>
    ) = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ) = itemCallback.areItemsTheSame(
            oldItems[oldItemPosition],
            newItems[newItemPosition]
        )

        override fun getOldListSize() = oldItems.size

        override fun getNewListSize() = newItems.size

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ) = itemCallback.areContentsTheSame(
            oldItems[oldItemPosition],
            newItems[newItemPosition]
        )

        override fun getChangePayload(
            oldItemPosition: Int,
            newItemPosition: Int
        ) = itemCallback.getChangePayload(
            oldItems[oldItemPosition],
            newItems[newItemPosition]
        )
    }, detectMoves)

    private fun latchList(newItems: List<T>, diffResult: DiffUtil.DiffResult) {
        items = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }
}
