package io.sungjk.asdf.test.retry

class RetryConfig {
    private val maxAttempts: Int = 3
    private val failAfterMaxAttempts: Boolean = false
    private val writableStackTraceEnabled: Boolean = true
}
