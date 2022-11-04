package io.sungjk.asdf.test.core.registry

import java.lang.reflect.Type
import java.time.ZonedDateTime

interface RegistryEvent {
    fun getEventType(): Type
    fun getCreationTime(): ZonedDateTime

    enum class Type {
        ADDED,
        REMOVED,
        REPLACED,
    }
}
