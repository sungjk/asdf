package io.sungjk.asdf.test

// 은행으로 송금을 수행하는 인터페이스
interface TransferBankUseCase {
    fun invoke(from: BankAccount, to: BankAccount, amount: Long): Result

    data class BankAccount(val bankCode: String, val accountNumber: String)
    sealed interface Result {
        data class Success(val transferHistoryId: Long) : Result
        data class Failure(val throwable: Throwable) : Result
    }
}

// 실제로 프로덕션에서 은행으로 송금하기 위해 사용되는 구체 클래스
class TransferBank(
    private val transferHistoryRepository: TransferHistoryRepository,
    private val bankPort: BankPort,
    private val emailPort: EmailPort,
) : TransferBankUseCase {
    override fun invoke(from: TransferBankUseCase.BankAccount, to: TransferBankUseCase.BankAccount, amount: Long): TransferBankUseCase.Result {
        if (from.bankCode == to.bankCode && from.accountNumber == to.accountNumber) {
            return TransferBankUseCase.Result.Failure(RuntimeException("동일 계좌로 송금 불가"))
        }
        // FROM 계좌의 잔액이 충분한지 검사
        val balanceOfFromBankAccount = bankPort.getBalance(from.bankCode, from.accountNumber)
        if (amount > balanceOfFromBankAccount) {
            return TransferBankUseCase.Result.Failure(RuntimeException("잔액 부족"))
        }

        // FROM 계좌에서 송금액만큼 출금
        bankPort.withdraw(bankCode = from.bankCode, accountNumber = from.accountNumber, amount = amount)

        // TO 계좌로 송금액만큼 입금
        val response = bankPort.deposit(bankCode = to.bankCode, accountNumber = to.accountNumber, amount = amount)
        return when (response.isSuccess()) {
            true -> {
                val transferHistory = transferHistoryRepository.save(
                    TransferHistory(
                        id = System.currentTimeMillis(),
                        fromBankCode = from.bankCode,
                        fromBankAccountNumber = from.accountNumber,
                        toBankCode = to.bankCode,
                        toBankAccountNumber = to.accountNumber,
                        amount = amount,
                    ),
                )
                emailPort.sendEmail(content = "송금 성공")
                TransferBankUseCase.Result.Success(transferHistoryId = transferHistory.id)
            }
            false -> {
                emailPort.sendEmail(content = "송금 실패")
                TransferBankUseCase.Result.Failure(throwable = RuntimeException(response.message))
            }
        }
    }
}
