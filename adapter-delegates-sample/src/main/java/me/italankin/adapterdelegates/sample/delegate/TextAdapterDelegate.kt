package me.italankin.adapterdelegates.sample.delegate

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.italankin.adapterdelegates.BaseAdapterDelegate
import me.italankin.adapterdelegates.sample.R
import me.italankin.adapterdelegates.sample.item.Text

class TextAdapterDelegate(
    private val onDeleteClick: (Int, Text) -> Unit
) : BaseAdapterDelegate<TextAdapterDelegate.ViewHolder, Text>() {

    override val layoutRes: Int = R.layout.item_text

    override fun isType(position: Int, item: Any): Boolean {
        return item is Text
    }

    override fun createViewHolder(itemView: View): ViewHolder {
        val holder = ViewHolder(itemView)
        holder.delete.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onDeleteClick(pos, getItem(pos))
            }
        }
        return holder
    }

    override fun onBind(holder: ViewHolder, position: Int, item: Text) {
        holder.text.text = item.text
    }

    override fun getItemId(position: Int, item: Text): Long {
        return item.id
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)
        val delete: View = itemView.findViewById(R.id.delete)
    }
}
