package io.sungjk.asdf.test.core

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.properties.Delegates

open class EventProcessor<T> : EventPublisher<T> {
    val onEventConsumers = CopyOnWriteArrayList<EventConsumer<T>>()
    val eventConsumerMap = ConcurrentHashMap<String, List<EventConsumer<T>>>()
    private var consumerRegistered by Delegates.notNull<Boolean>()

    @SuppressWarnings("unchecked")
    @Synchronized
    fun registerConsumer(className: String, eventConsumer: EventConsumer<T>) {
        // TODO Add Consumer
        this.consumerRegistered = true
    }

    fun <E : T> processEvent(event: E): Boolean {
        var consumed = false
        // TODO Calculate consumed variable
        return consumed
    }

    @Synchronized
    override fun onEvent(onEventConsumer: EventConsumer<T>) {
        this.onEventConsumers.add(onEventConsumer)
        this.consumerRegistered = true
    }
}
