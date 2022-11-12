package io.sungjk.asdf.test.circuitbreaker

import io.sungjk.asdf.test.core.Registry
import io.sungjk.asdf.test.core.registry.RegistryEventConsumer

interface CircuitBreakerRegistry : Registry<CircuitBreaker, CircuitBreakerConfig> {
    fun getAllCircuitBreakers(): Set<CircuitBreaker>
    fun circuitBreaker(name: String): CircuitBreaker
    fun circuitBreaker(name: String, tags: Map<String, String>): CircuitBreaker
    fun circuitBreaker(name: String, config: CircuitBreakerConfig): CircuitBreaker
    fun circuitBreaker(name: String, config: CircuitBreakerConfig, tags: Map<String, String>): CircuitBreaker
    fun circuitBreaker(name: String, configName: String): CircuitBreaker
    fun circuitBreaker(name: String, configName: String, tags: Map<String, String>): CircuitBreaker
    fun circuitBreaker(name: String, circuitBreakerConfigSupplier: () -> CircuitBreakerConfig): CircuitBreaker
    fun circuitBreaker(name: String, circuitBreakerConfigSupplier: () -> CircuitBreakerConfig, tags: Map<String, String>): CircuitBreaker

    companion object {
        fun of(circuitBreakerConfig: CircuitBreakerConfig): CircuitBreakerRegistry {
            TODO("InMemoryCircuitBreakerRegistry")
        }

        fun of(circuitBreakerConfig: CircuitBreakerConfig, registryEventConsumer: RegistryEventConsumer<CircuitBreaker>): CircuitBreakerRegistry {
            TODO("InMemoryCircuitBreakerRegistry")
        }

        fun of(circuitBreakerConfig: CircuitBreakerConfig, registryEventConsumers: List<RegistryEventConsumer<CircuitBreaker>>): CircuitBreakerRegistry {
            TODO("InMemoryCircuitBreakerRegistry")
        }

        fun of(configs: Map<String, CircuitBreakerConfig>): CircuitBreakerRegistry {
            return of(configs, emptyMap())
        }

        fun of(configs: Map<String, CircuitBreakerConfig>, tags: Map<String, String>): CircuitBreakerRegistry {
            TODO("InMemoryCircuitBreakerRegistry")
        }

        fun of(configs: Map<String, CircuitBreakerConfig>, registryEventConsumer: RegistryEventConsumer<CircuitBreaker>): CircuitBreakerRegistry {
            TODO("InMemoryCircuitBreakerRegistry")
        }

        fun of(configs: Map<String, CircuitBreakerConfig>, registryEventConsumer: RegistryEventConsumer<CircuitBreaker>, tags: Map<String, String>): CircuitBreakerRegistry {
            TODO("InMemoryCircuitBreakerRegistry")
        }

        fun of(configs: Map<String, CircuitBreakerConfig>, registryEventConsumers: List<RegistryEventConsumer<CircuitBreaker>>): CircuitBreakerRegistry {
            TODO("InMemoryCircuitBreakerRegistry")
        }

        fun ofDefaults(): CircuitBreakerRegistry {
            TODO("InMemoryCircuitBreakerRegistry")
        }
    }
}
