package io.sungjk.asdf.test

open class TransferHistoryRepositoryStub : TransferHistoryRepository {
    // ID : TransferHistory
    private var historyMap: MutableMap<Long, TransferHistory> = mutableMapOf()

    override fun findById(id: Long): TransferHistory? {
        return historyMap[id]
    }
    override fun save(history: TransferHistory): TransferHistory {
        historyMap[history.id] = history
        return history
    }
}
