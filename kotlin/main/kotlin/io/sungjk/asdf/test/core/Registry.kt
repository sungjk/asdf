package io.sungjk.asdf.test.core

import io.sungjk.asdf.test.core.registry.EntryAddedEvent
import io.sungjk.asdf.test.core.registry.EntryRemovedEvent
import io.sungjk.asdf.test.core.registry.EntryReplacedEvent
import io.sungjk.asdf.test.core.registry.RegistryEvent

interface Registry<E, C> {
    fun addConfiguration(configName: String, configuration: C)
    fun find(name: String): E?
    fun remove(name: String): E?
    fun replace(name: String, newEntry: E): E?
    fun getConfiguration(configName: String): C
    fun getDefaultConfig(): C
    fun getTags(): Map<String, String>
    fun getEventPublisher(): EventPublisher<E>

    interface EventPublisher<E> : io.sungjk.asdf.test.core.EventPublisher<RegistryEvent> {
        fun onEntryAdded(eventConsumer: EventConsumer<EntryAddedEvent<E>>)
        fun onEntryRemoved(eventConsumer: EventConsumer<EntryRemovedEvent<E>>)
        fun onEntryReplaced(eventConsumer: EventConsumer<EntryReplacedEvent<E>>)
    }
}
