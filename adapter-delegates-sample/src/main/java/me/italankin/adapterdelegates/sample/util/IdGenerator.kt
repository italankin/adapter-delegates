package me.italankin.adapterdelegates.sample.util

import java.util.concurrent.atomic.AtomicLong

object IdGenerator {

    private val counter = AtomicLong()

    fun get(): Long = counter.getAndIncrement()
}
