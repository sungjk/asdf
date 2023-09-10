package io.sungjk.asdf.test

// 은행 계좌를 다루기 위한 인터페이스
interface BankPort {
    // 계좌 잔액 조회
    fun getBalance(bankCode: String, accountNumber: String): Long

    // 계좌에서 금액을 출금
    fun withdraw(bankCode: String, accountNumber: String, amount: Long): Result

    // 계좌에 금액을 입금
    fun deposit(bankCode: String, accountNumber: String, amount: Long): Result

    data class Result(val resultCode: String, val message: String? = null) {
        fun isSuccess(): Boolean {
            return this.resultCode == "success"
        }
    }
}

// 은행에 각 기능을 요청하기 위해 HTTP 기반으로 구현한 구체 클래스
class BankHttpPort(private val httpClient: HttpClient) : BankPort {
    override fun getBalance(bankCode: String, accountNumber: String): Long {
        return httpClient.getBalance(bankCode, accountNumber)
    }

    override fun withdraw(bankCode: String, accountNumber: String, amount: Long): BankPort.Result {
        return httpClient.withdraw(bankCode, accountNumber, amount)
    }

    override fun deposit(bankCode: String, accountNumber: String, amount: Long): BankPort.Result {
        return httpClient.deposit(bankCode, accountNumber, amount)
    }
}

class HttpClient {
    fun getBalance(bankCode: String, accountNumber: String): Long {
        TODO("Not yet implemented")
    }

    fun withdraw(bankCode: String, accountNumber: String, amount: Long): BankPort.Result {
        TODO("Not yet implemented")
    }

    fun deposit(bankCode: String, accountNumber: String, amount: Long): BankPort.Result {
        TODO("Not yet implemented")
    }
}
