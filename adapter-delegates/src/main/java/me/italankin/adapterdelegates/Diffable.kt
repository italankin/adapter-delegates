package me.italankin.adapterdelegates

interface Diffable {

    /**
     * Unique ID for this item
     */
    val id: Long

    /**
     * Check whether content of this item is equal to [other]'s
     */
    fun contentEquals(other: Diffable): Boolean
}
