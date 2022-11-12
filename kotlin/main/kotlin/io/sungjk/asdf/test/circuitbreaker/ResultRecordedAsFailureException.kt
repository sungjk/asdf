package io.sungjk.asdf.test.circuitbreaker

import kotlinx.serialization.Transient

class ResultRecordedAsFailureException(
    circuitBreakerName: String,
    @Transient val result: Any,
) : RuntimeException("CircuitBreaker '$circuitBreakerName' has recorded '$result' as a failure")
