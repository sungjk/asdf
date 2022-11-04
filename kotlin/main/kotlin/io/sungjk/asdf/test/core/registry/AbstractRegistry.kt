package io.sungjk.asdf.test.core.registry

import io.sungjk.asdf.test.core.EventConsumer
import io.sungjk.asdf.test.core.EventProcessor
import io.sungjk.asdf.test.core.Registry
import io.sungjk.asdf.test.core.RegistryStore
import java.util.concurrent.ConcurrentMap

open class AbstractRegistry<E, C>(
    protected val entryMap: RegistryStore<E>,
    protected val configurations: ConcurrentMap<String, C>,
    protected val registryTags: Map<String, String>,
    private val eventProcessor: RegistryEventProcessor<E>,
) : Registry<E, C> {
    protected companion object A {
        const val DEFAULT_CONFIG = "default";
        const val CONFIG_MUST_NOT_BE_NULL = "Config must not be null";
        const val CONSUMER_MUST_NOT_BE_NULL = "EventConsumers must not be null";
        const val SUPPLIER_MUST_NOT_BE_NULL = "Supplier must not be null";
        const val TAGS_MUST_NOT_BE_NULL = "Tags must not be null";
        const val NAME_MUST_NOT_BE_NULL = "Name must not be null";
        const val REGISTRY_STORE_MUST_NOT_BE_NULL = "Registry Store must not be null";
    }

    override fun addConfiguration(configName: String, configuration: C) {
        TODO("Not yet implemented")
    }

    override fun find(name: String): E? {
        TODO("Not yet implemented")
    }

    override fun remove(name: String): E? {
        TODO("Not yet implemented")
    }

    override fun getConfiguration(configName: String): C {
        TODO("Not yet implemented")
    }

    override fun getDefaultConfig(): C {
        TODO("Not yet implemented")
    }

    override fun getTags(): Map<String, String> {
        TODO("Not yet implemented")
    }

    override fun getEventPublisher(): Registry.EventPublisher<E> {
        TODO("Not yet implemented")
    }

    override fun replace(name: String, newEntry: E): E? {
        TODO("Not yet implemented")
    }

    class RegistryEventProcessor<E> : EventProcessor<RegistryEvent>(), EventConsumer<RegistryEvent?>, Registry.EventPublisher<E> {
        override fun consumeEvent(event: RegistryEvent?) {
            TODO("Not yet implemented")
        }

        override fun onEntryAdded(eventConsumer: EventConsumer<EntryAddedEvent<E>>) {
            TODO("Not yet implemented")
        }

        override fun onEntryRemoved(eventConsumer: EventConsumer<EntryRemovedEvent<E>>) {
            TODO("Not yet implemented")
        }

        override fun onEntryReplaced(eventConsumer: EventConsumer<EntryReplacedEvent<E>>) {
            TODO("Not yet implemented")
        }
    }
}
