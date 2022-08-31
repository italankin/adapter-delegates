package me.italankin.adapterdelegates.sample.item

import me.italankin.adapterdelegate.Diffable
import me.italankin.adapterdelegates.sample.util.IdGenerator

class Title(
    val title: String
) : Diffable {

    override val id: Long = IdGenerator.get()

    override fun contentEquals(other: Diffable): Boolean {
        return other is Title && title == other.title
    }
}
