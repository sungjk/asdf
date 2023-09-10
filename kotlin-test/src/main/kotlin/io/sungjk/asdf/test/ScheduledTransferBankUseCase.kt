package io.sungjk.asdf.test

// 예약한 시간이 되면 은행으로 송금으로 수행하는 인터페이스
interface ScheduledTransferBankUseCase {
    fun invoke(from: BankAccount, to: BankAccount): Result

    data class BankAccount(val bankCode: String, val accountNumber: String)

    data class Result(val data: List<TransferResult>)

    sealed interface TransferResult {
        data class Success(val transferHistoryId: Long) : TransferResult
        data class Failure(val throwable: Throwable) : TransferResult
        data object Ignore : TransferResult
    }
}

// 실제로 프로덕션에서 은행으로 예약 송금을 하기 위해 사용되는 구체 클래스
class ScheduledTransferBank(
    private val scheduledTransferRepository: ScheduledTransferRepository,
    private val transferBankUseCase: TransferBankUseCase,
) : ScheduledTransferBankUseCase {
    override fun invoke(from: ScheduledTransferBankUseCase.BankAccount, to: ScheduledTransferBankUseCase.BankAccount): ScheduledTransferBankUseCase.Result {
        val now = System.currentTimeMillis()
        val result = scheduledTransferRepository.findAllByFromBankAccount(from.bankCode, from.accountNumber)
            .map {
                when (it.scheduledAt > now) {
                    true -> ScheduledTransferBankUseCase.TransferResult.Ignore
                    false -> {
                        val result = transferBankUseCase.invoke(
                            from = TransferBankUseCase.BankAccount(from.bankCode, from.accountNumber),
                            to = TransferBankUseCase.BankAccount(from.bankCode, from.accountNumber),
                            amount = it.amount,
                        )
                        when (result) {
                            is TransferBankUseCase.Result.Success -> ScheduledTransferBankUseCase.TransferResult.Success(result.transferHistoryId)
                            is TransferBankUseCase.Result.Failure -> ScheduledTransferBankUseCase.TransferResult.Failure(result.throwable)
                        }
                    }
                }
            }
        return ScheduledTransferBankUseCase.Result(result)
    }
}
