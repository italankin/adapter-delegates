package me.italankin.adapterdelegates.sample.util

import me.italankin.adapterdelegate.Diffable
import me.italankin.adapterdelegates.sample.item.Text
import me.italankin.adapterdelegates.sample.item.Title
import kotlin.random.Random

object RandomDataset {

    private val words = """lorem ipsum dolor sit amet consectetur adipiscing elit sed do
               eiusmod tempor incididunt ut labore et dolore magna aliqua ut enim ad
               minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip
               ex ea commodo consequat duis aute irure dolor in reprehenderit in voluptate
                velit esse cillum dolore eu fugiat nulla pariatur excepteur sint occaecat 
                cupidatat non proident sunt in culpa qui officia deserunt mollit anim 
                id est laborum""".split(Regex("\\s+"))


    fun get(): List<Diffable> {
        val result = ArrayList<Diffable>()
        for (i in randomRange(2, 5)) {
            result.add(Title(title = randomWords(4)))
            for (j in randomRange(1, 6)) {
                result.add(Text(text = randomWords(8, 25)))
            }
        }
        return result
    }

    private fun randomWords(min: Int, max: Int = min): String {
        val count = if (min == max) {
            min
        } else {
            min + Random.nextInt(max - min)
        }
        return (0..count).joinToString(separator = " ") { words[Random.nextInt(words.size)] }
            .replaceFirstChar(Char::uppercase)
    }

    private fun randomRange(min: Int, max: Int): IntRange {
        return IntRange(0, min + 1 + Random.nextInt(max - min))
    }
}
