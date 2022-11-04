package io.sungjk.asdf.test.core

interface EventConsumer<T> {
    fun consumeEvent(event: T)
}
