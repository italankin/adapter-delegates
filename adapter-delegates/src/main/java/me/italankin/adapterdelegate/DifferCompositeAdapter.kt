package me.italankin.adapterdelegate

import android.content.Context
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback

/**
 * A [CompositeAdapter] with [androidx.recyclerview.widget.DiffUtil] support.
 *
 * @param T item type of this adapter
 */
class DifferCompositeAdapter<T : Diffable> private constructor(
    context: Context,
    delegates: SparseArrayCompat<AdapterDelegate<*, *>>,
    hasStableIds: Boolean
) : CompositeAdapter<T>(context, delegates, hasStableIds) {

    private val differ: AsyncListDiffer<T> = AsyncListDiffer(this, object : ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.contentEquals(newItem)
        }
    })

    @Deprecated(
        "Use submitDataset() instead", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("submitDataset")
    )
    override fun setDataset(data: List<T>?) {
        throw UnsupportedOperationException("Use submitDataset() instead")
    }

    /**
     * Submit [data] to this adapter
     */
    fun submitDataset(data: List<T>) {
        if (data === dataset) {
            throw IllegalArgumentException("An original dataset should not be submitted, create a copy instead")
        }
        super.setDataset(data)
        differ.submitList(data)
    }

    /**
     * Submit [data] to this adapter, invoking [commitCallback] when changes are committed to adapter
     */
    fun submitDataset(data: List<T>, commitCallback: Runnable) {
        if (data === dataset) {
            throw IllegalArgumentException("An original dataset should not be submitted, create a copy instead")
        }
        super.setDataset(data)
        differ.submitList(data, commitCallback)
    }

    class Builder<T : Diffable>(context: Context) : BaseBuilder<T, Builder<T>, DifferCompositeAdapter<T>>(context) {

        @Deprecated("Use DifferCompositeAdapter#submitDataset instead", level = DeprecationLevel.ERROR)
        override fun dataset(dataset: List<T>): Builder<T> {
            throw UnsupportedOperationException("Use DifferCompositeAdapter#submitDataset instead")
        }

        override fun createAdapter(): DifferCompositeAdapter<T> {
            return DifferCompositeAdapter(context, delegates, hasStableIds)
        }
    }
}

