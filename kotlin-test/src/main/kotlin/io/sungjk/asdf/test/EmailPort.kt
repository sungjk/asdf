package io.sungjk.asdf.test

// 이메일을 발송하기 위한 인터페이스
interface EmailPort {
    fun sendEmail(content: String)
}

// 이메일을 발송하기 위해 SMTP 기반으로 구현한 인터페이스
class EmailSmtpPort(private val smtpClient: SmtpClient) : EmailPort {
    override fun sendEmail(content: String) {
        smtpClient.send(content)
    }
}

class SmtpClient {
    fun send(content: String) {
        TODO("Not yet implemented")
    }
}
