package io.sungjk.asdf.test.core.registry

class EntryReplacedEvent<E>(private val oldEntry: E, private val newEntry: E) : AbstractRegistryEvent() {
    override fun getEventType(): RegistryEvent.Type {
        return RegistryEvent.Type.REPLACED
    }

    fun getOldEntry(): E {
        return oldEntry
    }

    fun getNewEntry(): E {
        return newEntry
    }
}
