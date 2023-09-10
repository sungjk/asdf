package io.sungjk.asdf.test

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

// 예약 송금을 관리하기 위한 인터페이스
interface ScheduledTransferRepository {
    fun findAllByFromBankAccount(bankCode: String, accountNumber: String): List<ScheduledTransfer>
    fun save(schedule: ScheduledTransfer): ScheduledTransfer
}

data class ScheduledTransfer(
    val id: Long,
    val fromBankCode: String,
    val fromBankAccountNumber: String,
    val toBankCode: String,
    val toBankAccountNumber: String,
    val amount: Long,
    val scheduledAt: Long,
)

// 예약 송금을 관리하기 위해 Exposed 기반으로 구현한 구체 클래스
class ScheduledTransferRepositoryImpl : ScheduledTransferRepository {
    override fun findAllByFromBankAccount(bankCode: String, accountNumber: String): List<ScheduledTransfer> {
        return ScheduledTransferTable.select {
            ScheduledTransferTable.fromBankCode.eq(bankCode)
                .and(ScheduledTransferTable.fromBankAccountNumber.eq(accountNumber))
        }.map {
            ScheduledTransfer(
                id = it[ScheduledTransferTable.id].value,
                fromBankCode = it[ScheduledTransferTable.fromBankCode],
                fromBankAccountNumber = it[ScheduledTransferTable.fromBankAccountNumber],
                toBankCode = it[ScheduledTransferTable.toBankCode],
                toBankAccountNumber = it[ScheduledTransferTable.toBankAccountNumber],
                amount = it[ScheduledTransferTable.amount],
                scheduledAt = it[ScheduledTransferTable.scheduledAt],
            )
        }
    }

    override fun save(schedule: ScheduledTransfer): ScheduledTransfer {
        ScheduledTransferTable.insert {
            it[ScheduledTransferTable.id] = schedule.id
            it[fromBankCode] = schedule.fromBankCode
            it[fromBankAccountNumber] = schedule.fromBankAccountNumber
            it[toBankCode] = schedule.toBankCode
            it[toBankAccountNumber] = schedule.toBankAccountNumber
            it[amount] = schedule.amount
            it[scheduledAt] = schedule.scheduledAt
        }
        return schedule
    }
}

object ScheduledTransferTable : LongIdTable("scheduled_transfer", "id") {
    val fromBankCode = varchar("from_bank_code", 3)
    val fromBankAccountNumber = varchar("from_bank_account_number", 50)
    val toBankCode = varchar("to_bank_code", 3)
    val toBankAccountNumber = varchar("to_bank_account_number", 50)
    val amount = long("amount")
    val scheduledAt = long("scheduled_at")
}
