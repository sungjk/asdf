package io.sungjk.asdf.test.core

import java.util.function.Function
import kotlin.time.Duration
import kotlin.time.DurationUnit

fun interface IntervalFunction : Function<Int, Long> {

    companion object {
        private const val DEFAULT_INITIAL_INTERVAL = 500L

        fun ofDefaults(): IntervalFunction {
            return of(DEFAULT_INITIAL_INTERVAL)
        }

        fun of(intervalMillis: Long): IntervalFunction {
            checkInterval(intervalMillis)
            return IntervalFunction { attempt ->
                checkAttempt(attempt)
                intervalMillis
            }
        }

        fun of(interval: Duration): IntervalFunction {
            return of(interval.toLong(DurationUnit.MILLISECONDS))
        }

        private fun checkInterval(intervalMillis: Long) {
            if (intervalMillis < 1) {
                throw IllegalArgumentException("Illegal argument interval: $intervalMillis millis is less than 1")
            }
        }

        private fun checkAttempt(attempt: Int) {
            if (attempt < 1) {
                throw IllegalArgumentException("Illegal argument attempt: $attempt")
            }
        }
    }
}
