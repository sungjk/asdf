package io.sungjk.asdf.test.core

interface EventPublisher<T> {
    fun onEvent(onEventConsumer: EventConsumer<T>)
}
