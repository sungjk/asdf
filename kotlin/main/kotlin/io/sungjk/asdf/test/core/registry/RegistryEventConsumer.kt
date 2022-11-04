package io.sungjk.asdf.test.core.registry

interface RegistryEventConsumer<E> {
    fun onEntryAddedEvent(entryAddedEvent: EntryAddedEvent<E>)
    fun onEntryRemovedEvent(entryRemovedEvent: EntryRemovedEvent<E>)
    fun onEntryReplacedEvent(entryReplacedEvent: EntryReplacedEvent<E>)
}
