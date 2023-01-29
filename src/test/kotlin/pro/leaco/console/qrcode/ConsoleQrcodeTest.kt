package pro.leaco.console.qrcode

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ConsoleQrcodeTest {

    @Test
    fun print() {
        val content = "https://www.github.com"
        ConsoleQrcode.print(content)
    }

    @Test
    fun generateUnicodeQrcode() {
        val content = "https://www.github.com"
        val c = ConsoleQrcode.generateUnicodeQrcode(content)
        assertEquals(
            """                                 
                                 
    █▀▀▀▀▀█ ██▀▀ █▀▀▀ █▀▀▀▀▀█    
    █ ███ █   ▄▄ ██▄  █ ███ █    
    █ ▀▀▀ █ ▄█▀▀█▄▄▄  █ ▀▀▀ █    
    ▀▀▀▀▀▀▀ █▄█ ▀▄█ █ ▀▀▀▀▀▀▀    
    ▄ ▄▀▀▄▀▄▄▀█ ▄█  ▄▀█▀ ▄ ██    
    █▄██▀▀▀ ▀▀▄█▀▀▀▀  ▀▄▄▀▀ ▄    
    ▄█  ▀▄▀▀█ █▄ ▀▄▀█ ▀▄█▀██▄    
    ██▀ █▀▀▄▄▀ ▄▄▄██▄▀ ▄▀ █▄     
    ▀ ▀▀▀▀▀▀▀█▄█ █▄▄█▀▀▀█▄▄█▀    
    █▀▀▀▀▀█  ▄▀ ▀▄▀ █ ▀ █▀ █▄    
    █ ███ █ █▄▀▄▄█▀███▀▀█▄▀▀▀    
    █ ▀▀▀ █  ▀▀ ▀ ██     █▀▄     
    ▀▀▀▀▀▀▀  ▀  ▀ ▀▀▀▀▀  ▀▀ ▀    
                                 
""".trimIndent(), c.trimIndent()
        )
    }

}