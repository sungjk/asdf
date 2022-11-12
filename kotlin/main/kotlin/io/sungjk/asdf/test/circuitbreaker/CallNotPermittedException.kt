package io.sungjk.asdf.test.circuitbreaker

import kotlinx.serialization.Transient

class CallNotPermittedException(
    circuitBreaker: CircuitBreaker,
    override val message: String,
    writableStackTrace: Boolean,
) : RuntimeException(message, null, false, writableStackTrace) {
    @Transient
    val causingCircuitBreakerName = circuitBreaker.getName()

    companion object {
        fun createCallNotPermittedException(circuitBreaker: CircuitBreaker): CallNotPermittedException {
            val writableStackTraceEnabled = circuitBreaker.getCircuitBreakerConfig().writableStackTraceEnabled
            val message = "CircuitBreaker '${circuitBreaker.getName()}' is ${circuitBreaker.getState()} and does not permit further calls"
            return CallNotPermittedException(circuitBreaker, message, writableStackTraceEnabled)
        }
    }
}
