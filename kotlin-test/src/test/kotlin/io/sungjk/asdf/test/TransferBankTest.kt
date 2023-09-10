package io.sungjk.asdf.test

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class TransferBankTest : FunSpec() {
    init {
        test("FROM 계좌의 잔액이 부족하면 Failure 리턴(Using Stub)") {
            // arrange
            val sut = TransferBank(
                transferHistoryRepository = mockk(), // Dummy 객체
                bankPort = object : BankPort {
                    override fun getBalance(bankCode: String, accountNumber: String): Long {
                        return 1000L
                    }
                    override fun withdraw(bankCode: String, accountNumber: String, amount: Long): BankPort.Result {
                        TODO("Not yet implemented")
                    }
                    override fun deposit(bankCode: String, accountNumber: String, amount: Long): BankPort.Result {
                        TODO("Not yet implemented")
                    }
                },
                emailPort = mockk(), // Dummy 객체
            )

            // act
            val actual = sut.invoke(
                from = TransferBankUseCase.BankAccount("088", "1212121212"),
                to = TransferBankUseCase.BankAccount("088", "4242424242"),
                amount = 100_000L,
            )

            // assert
            (actual is TransferBankUseCase.Result.Failure) shouldBe true
        }

        test("FROM 계좌의 잔액이 부족하면 Failure 리턴(Using Mock)") {
            // arrange
            val bankPortMock = mockk<BankPort>()
            every { bankPortMock.getBalance(any(), any()) } returns 1000L
            val sut = TransferBank(
                transferHistoryRepository = mockk(),
                bankPort = bankPortMock,
                emailPort = mockk(),
            )

            // act
            val actual = sut.invoke(
                from = TransferBankUseCase.BankAccount("088", "1212121212"),
                to = TransferBankUseCase.BankAccount("088", "4242424242"),
                amount = 100_000L,
            )

            // assert
            (actual is TransferBankUseCase.Result.Failure) shouldBe true
        }

        test("송금을 성공하면 이메일을 한 번 발송(Using Stub)") {
            // arrange
            val transferHistoryRepositoryStub = object : TransferHistoryRepository {
                override fun findById(id: Long): TransferHistory = TODO("Not yet implemented")
                override fun save(history: TransferHistory): TransferHistory {
                    return history
                }
            }
            val bankPortStub = object : BankPort {
                override fun getBalance(bankCode: String, accountNumber: String): Long {
                    return 100_000L
                }
                override fun withdraw(bankCode: String, accountNumber: String, amount: Long): BankPort.Result {
                    return BankPort.Result("success")
                }
                override fun deposit(bankCode: String, accountNumber: String, amount: Long): BankPort.Result {
                    return BankPort.Result("success")
                }
            }
            val emailPortSpy = object : EmailPort {
                var emailCount = 0

                override fun sendEmail(content: String) {
                    emailCount++
                }
                fun countSentEmail(): Int {
                    return emailCount
                }
            }
            val sut = TransferBank(
                transferHistoryRepository = transferHistoryRepositoryStub,
                bankPort = bankPortStub,
                emailPort = emailPortSpy,
            )

            // act
            val actual = sut.invoke(
                from = TransferBankUseCase.BankAccount("088", "1212121212"),
                to = TransferBankUseCase.BankAccount("088", "4242424242"),
                amount = 100_000L,
            )

            // assert
            check(actual is TransferBankUseCase.Result.Success)
            emailPortSpy.countSentEmail() shouldBe 1
        }

        test("송금을 성공하면 이메일을 한 번 발송(Using Mock)") {
            val transferHistoryRepositoryMock = mockk<TransferHistoryRepository>()
            every { transferHistoryRepositoryMock.save(any()) } returns TransferHistory(
                id = 1L,
                fromBankCode = "088",
                fromBankAccountNumber = "1212121212",
                toBankCode = "088",
                toBankAccountNumber = "4242424242",
                amount = 100_000L,
            )
            val bankPortMock = mockk<BankPort>()
            every { bankPortMock.getBalance(any(), any()) } returns 100_000L
            every { bankPortMock.withdraw(any(), any(), any()) } returns BankPort.Result("success")
            every { bankPortMock.deposit(any(), any(), any()) } returns BankPort.Result("success")
            val emailPort = mockk<EmailPort>()
            every { emailPort.sendEmail(any()) } returns Unit
            val sut = TransferBank(
                transferHistoryRepository = transferHistoryRepositoryMock,
                bankPort = bankPortMock,
                emailPort = emailPort,
            )

            // act
            sut.invoke(
                from = TransferBankUseCase.BankAccount("088", "1212121212"),
                to = TransferBankUseCase.BankAccount("088", "4242424242"),
                amount = 100_000L,
            )

            // assert
            verify(exactly = 1) {
                emailPort.sendEmail(any())
            }
        }

        test("송금을 성공하면 송금 히스토리 저장(Using Stub)") {
            // arrange
            val transferHistoryRepositoryStub = object : TransferHistoryRepository {
                var historyMap: MutableMap<Long, TransferHistory> = mutableMapOf()

                override fun findById(id: Long): TransferHistory? {
                    return historyMap[id]
                }
                override fun save(history: TransferHistory): TransferHistory {
                    historyMap[history.id] = history
                    return history
                }
            }
            val bankPortStub = object : BankPort {
                override fun getBalance(bankCode: String, accountNumber: String): Long {
                    return 100_000L
                }
                override fun withdraw(bankCode: String, accountNumber: String, amount: Long): BankPort.Result {
                    return BankPort.Result("success")
                }
                override fun deposit(bankCode: String, accountNumber: String, amount: Long): BankPort.Result {
                    return BankPort.Result("success")
                }
            }
            val emailPortSpy = object : EmailPort {
                override fun sendEmail(content: String) {}
            }
            val sut = TransferBank(
                transferHistoryRepository = transferHistoryRepositoryStub,
                bankPort = bankPortStub,
                emailPort = emailPortSpy,
            )

            // act
            val actual = sut.invoke(
                from = TransferBankUseCase.BankAccount("088", "1212121212"),
                to = TransferBankUseCase.BankAccount("088", "4242424242"),
                amount = 100_000L,
            )

            // assert
            check(actual is TransferBankUseCase.Result.Success)
            val transferHistory = transferHistoryRepositoryStub.findById(actual.transferHistoryId)
            transferHistory shouldNotBe null
        }

        test("송금 성공(Using Mock)") {
            // arrange
            val transferHistoryRepositoryMock = mockk<TransferHistoryRepository>()
            every { transferHistoryRepositoryMock.save(any()) } returns TransferHistory(
                id = 1L,
                fromBankCode = "088",
                fromBankAccountNumber = "1212121212",
                toBankCode = "088",
                toBankAccountNumber = "4242424242",
                amount = 100_000L,
            )
            val bankPortMock = mockk<BankPort>()
            every { bankPortMock.getBalance(any(), any()) } returns 100_000L
            every { bankPortMock.withdraw(any(), any(), any()) } returns BankPort.Result("success")
            every { bankPortMock.deposit(any(), any(), any()) } returns BankPort.Result("success")
            val emailPort = mockk<EmailPort>()
            every { emailPort.sendEmail(any()) } returns Unit
            val sut = TransferBank(
                transferHistoryRepository = transferHistoryRepositoryMock,
                bankPort = bankPortMock,
                emailPort = emailPort,
            )

            // act
            val actual = sut.invoke(
                from = TransferBankUseCase.BankAccount("088", "1212121212"),
                to = TransferBankUseCase.BankAccount("088", "4242424242"),
                amount = 100_000L,
            )

            // assert
            (actual is TransferBankUseCase.Result.Success) shouldBe true
        }
    }
}
