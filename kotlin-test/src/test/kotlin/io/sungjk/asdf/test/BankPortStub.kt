package io.sungjk.asdf.test

open class BankPortStub(private val throwable: Throwable? = null) : BankPort {
    // 은행 계좌별 잔액
    private var bankAccountMap: MutableMap<Pair<String, String>, Long> = mutableMapOf()

    override fun getBalance(bankCode: String, accountNumber: String): Long {
        return bankAccountMap[Pair(bankCode, accountNumber)] ?: 0L
    }

    override fun withdraw(bankCode: String, accountNumber: String, amount: Long): BankPort.Result {
        val currentBalance = bankAccountMap[Pair(bankCode, accountNumber)] ?: 0L
        if (amount > currentBalance) {
            return BankPort.Result("failure", "잔액 부족")
        }
        if (throwable != null) {
            return BankPort.Result("failure", throwable.message)
        }
        bankAccountMap[Pair(bankCode, accountNumber)] = currentBalance - amount
        return BankPort.Result("success")
    }

    override fun deposit(bankCode: String, accountNumber: String, amount: Long): BankPort.Result {
        if (throwable != null) {
            return BankPort.Result("failure", throwable.message)
        }
        val currentBalance = bankAccountMap[Pair(bankCode, accountNumber)] ?: 0L
        bankAccountMap[Pair(bankCode, accountNumber)] = currentBalance + amount
        return BankPort.Result("success")
    }
}
