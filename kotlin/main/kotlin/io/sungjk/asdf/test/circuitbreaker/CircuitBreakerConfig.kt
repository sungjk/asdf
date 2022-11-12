package io.sungjk.asdf.test.circuitbreaker

import io.sungjk.asdf.test.core.IntervalFunction
import kotlinx.serialization.Serializable
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Serializable
class CircuitBreakerConfig(
    val failureRateThreshold: Float = DEFAULT_FAILURE_RATE_THRESHOLD,
    val permittedNumberOfCallsInHalfOpenState: Int = DEFAULT_PERMITTED_CALLS_IN_HALF_OPEN_STATE,
    val slidingWindowSize: Int = DEFAULT_SLIDING_WINDOW_SIZE,
    val slidingWindowType: SlidingWindowType = DEFAULT_SLIDING_WINDOW_TYPE,
    val minimumNumberOfCalls: Int = DEFAULT_MINIMUM_NUMBER_OF_CALLS,
    val writableStackTraceEnabled: Boolean = DEFAULT_WRITABLE_STACK_TRACE_ENABLED,
    val automaticTransitionFromOpenToHalfOpenEnabled: Boolean = false,
) {
    @Transient
    private val waitIntervalFunctionInOpenState = IntervalFunction.of(DEFAULT_WAIT_DURATION_IN_OPEN_STATE.toDuration(DurationUnit.SECONDS))
    private val slowCallRateThreshold = DEFAULT_SLOW_CALL_RATE_THRESHOLD
    private val slowCallDurationThreshold = DEFAULT_SLOW_CALL_DURATION_THRESHOLD.toDuration(DurationUnit.SECONDS)
    private val maxWaitDurationInHalfOpenState = DEFAULT_WAIT_DURATION_IN_HALF_OPEN_STATE.toDuration(DurationUnit.SECONDS)

    enum class SlidingWindowType {
        TIME_BASED, COUNT_BASED
    }

    companion object {
        const val DEFAULT_FAILURE_RATE_THRESHOLD = 50.0f // Percentage
        const val DEFAULT_SLOW_CALL_RATE_THRESHOLD = 100.0f // Percentage
        const val DEFAULT_WAIT_DURATION_IN_OPEN_STATE = 60 // Seconds
        const val DEFAULT_PERMITTED_CALLS_IN_HALF_OPEN_STATE = 10
        const val DEFAULT_MINIMUM_NUMBER_OF_CALLS = 100
        const val DEFAULT_SLIDING_WINDOW_SIZE = 100
        const val DEFAULT_SLOW_CALL_DURATION_THRESHOLD = 60
        const val DEFAULT_WAIT_DURATION_IN_HALF_OPEN_STATE = 0
        const val DEFAULT_WRITABLE_STACK_TRACE_ENABLED = true
        val DEFAULT_SLIDING_WINDOW_TYPE = SlidingWindowType.COUNT_BASED

        fun ofDefaults(): CircuitBreakerConfig {
            return CircuitBreakerConfig()
        }
    }
}
