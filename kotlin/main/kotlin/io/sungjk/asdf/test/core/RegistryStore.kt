package io.sungjk.asdf.test.core

interface RegistryStore<E> {
    fun computeIfAbsent(key: String, mappingFunction: (String) -> E): E
    fun putIfAbsent(key: String, value: E): E
    fun find(key: String): E?
    fun remove(name: String): E?
    fun remove(name: String, newEntry: E): E?
    fun values(): Collection<E>
}
