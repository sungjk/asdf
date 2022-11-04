package io.sungjk.asdf.test.core.registry

import java.time.ZonedDateTime

abstract class AbstractRegistryEvent(private val creationTime: ZonedDateTime) : RegistryEvent {
    constructor() : this(ZonedDateTime.now())

    override fun getCreationTime(): ZonedDateTime {
        return creationTime
    }
}
