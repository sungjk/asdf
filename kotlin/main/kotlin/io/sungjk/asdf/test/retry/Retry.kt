package io.sungjk.asdf.test.retry

interface Retry {
    companion object {
        fun of(name: String, retryConfig: RetryConfig): Retry {
            TODO("RetryImpl")
        }
    }

    fun getName(): String
    fun <T> context(): Context<T>

    interface Context<T> {
        fun onComplete()
        fun onResult(result: T): Boolean
        @Throws(Exception::class)
        fun onError(exception: Exception?)
        fun onRuntimeError(runtimeException: RuntimeException)
    }

    // TODO declare EventPublisher interface

    // TODO declare RetryEvent class
}
