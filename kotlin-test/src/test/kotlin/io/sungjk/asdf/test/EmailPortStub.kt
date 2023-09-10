package io.sungjk.asdf.test

class EmailPortStub : EmailPort {
    private var emailCount = 0

    override fun sendEmail(content: String) {
        emailCount++
    }

    fun countSentEmail(): Int {
        return emailCount
    }
}
