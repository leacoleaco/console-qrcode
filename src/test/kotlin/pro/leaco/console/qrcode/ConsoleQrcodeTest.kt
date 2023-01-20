package pro.leaco.console.qrcode

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class ConsoleQrcodeTest {

    @Test
    fun print() {
        var content = "https://www.github.com"
        ConsoleQrcode.print(content)
    }
}