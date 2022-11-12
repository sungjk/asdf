package io.sungjk.asdf.test.circuitbreaker

class IllegalStateTransitionException(
    name: String,
    fromState: CircuitBreaker.State,
    toState: CircuitBreaker.State,
) : RuntimeException("CircuitBreaker '$name' tried an illegal state transition from $fromState to $toState")
