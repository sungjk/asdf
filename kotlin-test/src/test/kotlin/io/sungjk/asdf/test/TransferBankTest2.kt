package io.sungjk.asdf.test

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class TransferBankTest2 : FunSpec() {
    init {
        val givenFromBankAccount = TransferBankUseCase.BankAccount("088", "1212121212")
        val givenToBankAccount = TransferBankUseCase.BankAccount("088", "4242424242")

        test("FROM 계좌의 잔액이 부족하면 Failure 리턴") {
            // arrange
            val sut = TransferBank(
                transferHistoryRepository = TransferHistoryRepositoryStub(),
                bankPort = BankPortStub(),
                emailPort = EmailPortStub(),
            )

            // act
            val actual = sut.invoke(
                from = givenFromBankAccount,
                to = givenToBankAccount,
                amount = 100_000L,
            )

            // assert
            (actual is TransferBankUseCase.Result.Failure) shouldBe true
        }

        test("송금을 성공하면 이메일을 한 번 발송") {
            // arrange
            val bankPortStub = BankPortStub()
            bankPortStub.deposit(givenFromBankAccount.bankCode, givenFromBankAccount.accountNumber, 100_000L)
            val emailPortSpy = EmailPortStub()
            val sut = TransferBank(
                transferHistoryRepository = TransferHistoryRepositoryStub(),
                bankPort = bankPortStub,
                emailPort = emailPortSpy,
            )

            // act
            val actual = sut.invoke(
                from = givenFromBankAccount,
                to = givenToBankAccount,
                amount = 100_000L,
            )

            // assert
            check(actual is TransferBankUseCase.Result.Success)
            emailPortSpy.countSentEmail() shouldBe 1
        }

        test("송금을 성공하면 송금 히스토리 저장") {
            // arrange
            val bankPortStub = BankPortStub()
            bankPortStub.deposit(givenFromBankAccount.bankCode, givenFromBankAccount.accountNumber, 100_000L)
            val transferHistoryRepositorySpy = TransferHistoryRepositoryStub()
            val sut = TransferBank(
                transferHistoryRepository = transferHistoryRepositorySpy,
                bankPort = bankPortStub,
                emailPort = EmailPortStub(),
            )

            // act
            val actual = sut.invoke(
                from = givenFromBankAccount,
                to = givenToBankAccount,
                amount = 100_000L,
            )

            // assert
            check(actual is TransferBankUseCase.Result.Success)
            val transferHistory = transferHistoryRepositorySpy.findById(actual.transferHistoryId)
            transferHistory shouldNotBe null
        }

        test("송금 성공") {
            // arrange
            val bankPortStub = BankPortStub()
            bankPortStub.deposit(givenFromBankAccount.bankCode, givenFromBankAccount.accountNumber, 100_000L)
            val sut = TransferBank(
                transferHistoryRepository = TransferHistoryRepositoryStub(),
                bankPort = bankPortStub,
                emailPort = EmailPortStub(),
            )

            // act
            val actual = sut.invoke(
                from = givenFromBankAccount,
                to = givenToBankAccount,
                amount = 100_000L,
            )

            // assert
            (actual is TransferBankUseCase.Result.Success) shouldBe true
        }
    }
}
