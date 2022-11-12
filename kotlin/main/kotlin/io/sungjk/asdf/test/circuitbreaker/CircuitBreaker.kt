package io.sungjk.asdf.test.circuitbreaker

import java.time.Instant

interface CircuitBreaker {
    fun tryAcquirePermission(): Boolean
    fun releasePermission(): Boolean
    fun acquirePermission(): Boolean
    fun onError(durationMillis: Long, throwable: Throwable)
    fun onSuccess(durationMillis: Long)
    fun onResult(durationMillis: Long, result: Any)
    fun reset()
    fun transitionToClosedState()
    fun transitionToOpenState()
    fun transitionToOpenStateFor(waitDurationMillis: Long)
    fun transitionToOpenStateUntil(waitUntil: Instant)
    fun transitionToHalfOpenState()
    fun transitionToDisabledState()
    fun transitionToMetricsOnlyState()
    fun transitionToForcedOpenState()
    fun getName(): String
    fun getState(): String
    fun getCircuitBreakerConfig(): CircuitBreakerConfig
    fun getTags(): Map<String, String>
    fun getCurrentTimestamp(): Long

    enum class State(val order: Int, val allowPublish: Boolean) {
        CLOSED(0, true),
        OPEN(1, true),
        HALF_OPEN(2, true),
        DISABLED(3, false),
        FORCED_OPEN(4, false),
        METRICS_ONLY(5, true),
        ;
    }

    enum class StateTransition(val fromState: State, val toState: State) {
        CLOSED_TO_CLOSED(State.CLOSED, State.CLOSED),
        CLOSED_TO_OPEN(State.CLOSED, State.OPEN),
        CLOSED_TO_DISABLED(State.CLOSED, State.DISABLED),
        CLOSED_TO_METRICS_ONLY(State.CLOSED, State.METRICS_ONLY),
        CLOSED_TO_FORCED_OPEN(State.CLOSED, State.FORCED_OPEN),
        HALF_OPEN_TO_HALF_OPEN(State.HALF_OPEN, State.HALF_OPEN),
        HALF_OPEN_TO_CLOSED(State.HALF_OPEN, State.CLOSED),
        HALF_OPEN_TO_OPEN(State.HALF_OPEN, State.OPEN),
        HALF_OPEN_TO_DISABLED(State.HALF_OPEN, State.DISABLED),
        HALF_OPEN_TO_METRICS_ONLY(State.HALF_OPEN, State.METRICS_ONLY),
        HALF_OPEN_TO_FORCED_OPEN(State.HALF_OPEN, State.FORCED_OPEN),
        OPEN_TO_OPEN(State.OPEN, State.OPEN),
        OPEN_TO_CLOSED(State.OPEN, State.CLOSED),
        OPEN_TO_HALF_OPEN(State.OPEN, State.HALF_OPEN),
        OPEN_TO_DISABLED(State.OPEN, State.DISABLED),
        OPEN_TO_METRICS_ONLY(State.OPEN, State.METRICS_ONLY),
        OPEN_TO_FORCED_OPEN(State.OPEN, State.FORCED_OPEN),
        FORCED_OPEN_TO_FORCED_OPEN(State.FORCED_OPEN, State.FORCED_OPEN),
        FORCED_OPEN_TO_CLOSED(State.FORCED_OPEN, State.CLOSED),
        FORCED_OPEN_TO_OPEN(State.FORCED_OPEN, State.OPEN),
        FORCED_OPEN_TO_DISABLED(State.FORCED_OPEN, State.DISABLED),
        FORCED_OPEN_TO_METRICS_ONLY(State.FORCED_OPEN, State.METRICS_ONLY),
        FORCED_OPEN_TO_HALF_OPEN(State.FORCED_OPEN, State.HALF_OPEN),
        DISABLED_TO_DISABLED(State.DISABLED, State.DISABLED),
        DISABLED_TO_CLOSED(State.DISABLED, State.CLOSED),
        DISABLED_TO_OPEN(State.DISABLED, State.OPEN),
        DISABLED_TO_FORCED_OPEN(State.DISABLED, State.FORCED_OPEN),
        DISABLED_TO_HALF_OPEN(State.DISABLED, State.HALF_OPEN),
        DISABLED_TO_METRICS_ONLY(State.DISABLED, State.METRICS_ONLY),
        METRICS_ONLY_TO_METRICS_ONLY(State.METRICS_ONLY, State.METRICS_ONLY),
        METRICS_ONLY_TO_CLOSED(State.METRICS_ONLY, State.CLOSED),
        METRICS_ONLY_TO_FORCED_OPEN(State.METRICS_ONLY, State.FORCED_OPEN),
        METRICS_ONLY_TO_DISABLED(State.METRICS_ONLY, State.DISABLED),
        ;

        override fun toString(): String {
            return "State transition from $fromState to $toState"
        }

        companion object {
            fun isInternalTransition(transition: StateTransition): Boolean {
                return transition.toState == transition.fromState
            }
        }
    }
}
