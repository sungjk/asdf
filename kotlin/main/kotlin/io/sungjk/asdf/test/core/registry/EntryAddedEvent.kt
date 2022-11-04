package io.sungjk.asdf.test.core.registry

class EntryAddedEvent<E>(private val addedEntry: E) : AbstractRegistryEvent() {
    override fun getEventType(): RegistryEvent.Type {
        return RegistryEvent.Type.ADDED
    }

    fun getAddedEntry(): E {
        return addedEntry
    }
}
