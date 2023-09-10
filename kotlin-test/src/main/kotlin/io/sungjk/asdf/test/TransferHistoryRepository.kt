package io.sungjk.asdf.test

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

// 송금 기록을 관리하기 위한 인터페이스
interface TransferHistoryRepository {
    fun findById(id: Long): TransferHistory?
    fun save(history: TransferHistory): TransferHistory
}

data class TransferHistory(
    val id: Long,
    val fromBankCode: String,
    val fromBankAccountNumber: String,
    val toBankCode: String,
    val toBankAccountNumber: String,
    val amount: Long,
)

// 송금 기록을 관리하기 위해 Exposed 기반으로 구현한 구체 클래스
class TransferHistoryRepositoryImpl : TransferHistoryRepository {
    override fun findById(id: Long): TransferHistory? {
        return TransferHistoryTable.select {
            TransferHistoryTable.id.eq(id)
        }.map {
            TransferHistory(
                id = it[TransferHistoryTable.id].value,
                fromBankCode = it[TransferHistoryTable.fromBankCode],
                fromBankAccountNumber = it[TransferHistoryTable.fromBankAccountNumber],
                toBankCode = it[TransferHistoryTable.toBankCode],
                toBankAccountNumber = it[TransferHistoryTable.toBankAccountNumber],
                amount = it[TransferHistoryTable.amount],
            )
        }.firstOrNull()
    }

    override fun save(history: TransferHistory): TransferHistory {
        TransferHistoryTable.insert {
            it[TransferHistoryTable.id] = history.id
            it[fromBankCode] = history.fromBankCode
            it[fromBankAccountNumber] = history.fromBankAccountNumber
            it[toBankCode] = history.toBankCode
            it[toBankAccountNumber] = history.toBankAccountNumber
            it[amount] = history.amount
        }
        return history
    }
}

object TransferHistoryTable : LongIdTable("transfer_history", "id") {
    val fromBankCode = varchar("from_bank_code", 3)
    val fromBankAccountNumber = varchar("from_bank_account_number", 50)
    val toBankCode = varchar("to_bank_code", 3)
    val toBankAccountNumber = varchar("to_bank_account_number", 50)
    val amount = long("amount")
}
