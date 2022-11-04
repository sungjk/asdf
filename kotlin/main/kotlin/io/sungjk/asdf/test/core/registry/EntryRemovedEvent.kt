package io.sungjk.asdf.test.core.registry

class EntryRemovedEvent<E>(private val removedEntry: E) : AbstractRegistryEvent() {
    override fun getEventType(): RegistryEvent.Type {
        return RegistryEvent.Type.REMOVED
    }

    fun getRemovedEntry(): E {
        return removedEntry
    }
}
