package pro.leaco.console.qrcode

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitArray
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.CharacterSetECI
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.qrcode.decoder.Mode
import com.google.zxing.qrcode.decoder.Version
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.util.*

object ConsoleQrcode {
    private const val BLACK = "\u001b[40m  \u001b[0m"
    private const val WHITE = "\u001b[47m  \u001b[0m"
    private const val DEFAULT_BYTE_MODE_ENCODING = "ISO-8859-1"
    private val ALPHANUMERIC_TABLE = intArrayOf(
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  // 0x00-0x0f
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  // 0x10-0x1f
        36, -1, -1, -1, 37, 38, -1, -1, -1, -1, 39, 40, -1, 41, 42, 43,  // 0x20-0x2f
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 44, -1, -1, -1, -1, -1,  // 0x30-0x3f
        -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,  // 0x40-0x4f
        25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1
    )

    /**
     * print qrcode in console
     *
     * @param content the qrcode's content
     */
    fun print(content: String) {
        generateQrcodeBitMatrix(content) { maxWidth, bitMatrix ->
            for (i in 0 until maxWidth) {
                for (j in 0 until maxWidth) {
                    kotlin.io.print(if (bitMatrix[i, j]) BLACK else WHITE)
                }
                print('\n')
            }
        }
    }

    /**
     * convert qrcode to the unicode string
     * e.g.
     * ```text
     *  █▀▀▀▀▀█ ██▀▀ █▀▀▀ █▀▀▀▀▀█
     *  █ ███ █   ▄▄ ██▄  █ ███ █
     *  █ ▀▀▀ █ ▄█▀▀█▄▄▄  █ ▀▀▀ █
     *  ▀▀▀▀▀▀▀ █▄█ ▀▄█ █ ▀▀▀▀▀▀▀
     *  ▄ ▄▀▀▄▀▄▄▀█ ▄█  ▄▀█▀ ▄ ██
     *  █▄██▀▀▀ ▀▀▄█▀▀▀▀  ▀▄▄▀▀ ▄
     *  ▄█  ▀▄▀▀█ █▄ ▀▄▀█ ▀▄█▀██▄
     *  ██▀ █▀▀▄▄▀ ▄▄▄██▄▀ ▄▀ █▄
     *  ▀ ▀▀▀▀▀▀▀█▄█ █▄▄█▀▀▀█▄▄█▀
     *  █▀▀▀▀▀█  ▄▀ ▀▄▀ █ ▀ █▀ █▄
     *  █ ███ █ █▄▀▄▄█▀███▀▀█▄▀▀▀
     *  █ ▀▀▀ █  ▀▀ ▀ ██     █▀▄
     *  ▀▀▀▀▀▀▀  ▀  ▀ ▀▀▀▀▀  ▀▀ ▀
     *  ```
     *
     * @param content the qrcode's content
     * @return unicode string
     */
    fun generateUnicodeQrcode(content: String): String {
        val r = StringBuilder()
        generateQrcodeBitMatrix(content) { maxWidth, bitMatrix ->
            for (i in 0 until maxWidth - 1 step 2) {
                for (j in 0 until maxWidth) {
                    val t = if (bitMatrix[i, j]) 0b10 else 0
                    val b = if (bitMatrix[i + 1, j]) 0b01 else 0
                    val c = when (val s = t + b) {
                        0b00 -> ' '
                        0b10 -> '\u2580'
                        0b01 -> '\u2584'
                        0b11 -> '\u2588'
                        else -> throw Error("impossible map: ${s.toString(2)}")
                    }
                    r.append(c)
                }
                r.append('\n')
            }
        }
        return r.toString()
    }

    private fun generateQrcodeBitMatrix(content: String, block: (maxWidth: Int, bitMatrix: BitMatrix) -> Unit) {
        val hints: MutableMap<EncodeHintType, Any?> = EnumMap(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"

        var maxWidth = 8
        try {
            maxWidth += getVersion(content, hints)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        val width = maxWidth
        val height = maxWidth
        val bitMatrix = MultiFormatWriter().encode(
            content,
            BarcodeFormat.QR_CODE, width, height, hints
        )
        block.invoke(maxWidth, bitMatrix)
    }


    @Throws(WriterException::class)
    private fun getVersion(content: String, hints: Map<EncodeHintType, Any?>?): Int {

        // Determine what character encoding has been specified by the caller, if any
        var encoding = "ISO-8859-1"
        if (hints != null && hints.containsKey(EncodeHintType.CHARACTER_SET)) {
            encoding = hints[EncodeHintType.CHARACTER_SET].toString()
        }

        // Pick an encoding mode appropriate for the content. Note that this will not attempt to use
        // multiple modes / segments even if that were more efficient. Twould be nice.
        val mode = chooseMode(content, encoding)

        // This will store the header information, like mode and
        // length, as well as "header" segments like an ECI segment.
        val headerBits = BitArray()

        // Append ECI segment if applicable
        if (mode == Mode.BYTE && DEFAULT_BYTE_MODE_ENCODING != encoding) {
            val eci = CharacterSetECI.getCharacterSetECIByName(encoding)
            if (eci != null) {
                appendECI(eci, headerBits)
            }
        }

        // (With ECI in place,) Write the mode marker
        appendModeInfo(mode, headerBits)

        // Collect data within the main segment, separately, to count its size if needed. Don't add it to
        // main payload yet.
        val dataBits = BitArray()
        appendBytes(content, mode, dataBits, encoding)
        val provisionalBitsNeeded = calculateBitsNeeded(mode, headerBits, dataBits, Version.getVersionForNumber(1))
        for (versionNum in 1..40) {
            val version = Version.getVersionForNumber(versionNum)
            if (willFit(provisionalBitsNeeded, version, ErrorCorrectionLevel.L)) {
                return version.versionNumber * 4 + 17
            }
        }
        return 177
        /*
        double d = Math.sqrt(contentLength * 8 + 209);
        int version = (int)Math.ceil(d) - 20;
        if(version <=0 ) version = 1;
        if(version > 40 ) version = 40;
        return 8 * 4 + 17;
        */
    }

    private fun willFit(numInputBits: Int, version: Version, ecLevel: ErrorCorrectionLevel?): Boolean {
        // In the following comments, we use numbers of Version 7-H.
        // numBytes = 196
        val numBytes = version.totalCodewords
        // getNumECBytes = 130
        val ecBlocks = version.getECBlocksForLevel(ecLevel)
        val numEcBytes = ecBlocks.totalECCodewords
        // getNumDataBytes = 196 - 130 = 66
        val numDataBytes = numBytes - numEcBytes
        val totalInputBytes = (numInputBits + 7) / 8
        return numDataBytes >= totalInputBytes
    }

    private fun calculateBitsNeeded(
        mode: Mode,
        headerBits: BitArray,
        dataBits: BitArray,
        version: Version?
    ): Int {
        return headerBits.size + mode.getCharacterCountBits(version) + dataBits.size
    }

    private fun chooseMode(content: String, encoding: String): Mode {
        if ("Shift_JIS" == encoding && isOnlyDoubleByteKanji(content)) {
            // Choose Kanji mode if all input are double-byte characters
            return Mode.KANJI
        }
        var hasNumeric = false
        var hasAlphanumeric = false
        for (element in content) {
            if (element in '0'..'9') {
                hasNumeric = true
            } else if (getAlphanumericCode(element.code) != -1) {
                hasAlphanumeric = true
            } else {
                return Mode.BYTE
            }
        }
        if (hasAlphanumeric) {
            return Mode.ALPHANUMERIC
        }
        return if (hasNumeric) {
            Mode.NUMERIC
        } else Mode.BYTE
    }

    private fun isOnlyDoubleByteKanji(content: String): Boolean {
        val bytes: ByteArray = try {
            content.toByteArray(charset("Shift_JIS"))
        } catch (ignored: UnsupportedEncodingException) {
            return false
        }
        val length = bytes.size
        if (length % 2 != 0) {
            return false
        }
        var i = 0
        while (i < length) {
            val byte1 = bytes[i].toInt() and 0xFF
            if ((byte1 < 0x81 || byte1 > 0x9F) && (byte1 < 0xE0 || byte1 > 0xEB)) {
                return false
            }
            i += 2
        }
        return true
    }

    private fun getAlphanumericCode(code: Int): Int {
        return if (code < ALPHANUMERIC_TABLE.size) {
            ALPHANUMERIC_TABLE[code]
        } else -1
    }

    @Throws(WriterException::class)
    fun appendBytes(
        content: String,
        mode: Mode,
        bits: BitArray,
        encoding: String?
    ) {
        when (mode) {
            Mode.NUMERIC -> appendNumericBytes(content, bits)
            Mode.ALPHANUMERIC -> appendAlphanumericBytes(content, bits)
            Mode.BYTE -> append8BitBytes(content, bits, encoding)
            Mode.KANJI -> appendKanjiBytes(content, bits)
            else -> throw WriterException("Invalid mode: $mode")
        }
    }

    private fun appendNumericBytes(content: CharSequence, bits: BitArray) {
        val length = content.length
        var i = 0
        while (i < length) {
            val num1 = content[i].code - '0'.code
            if (i + 2 < length) {
                // Encode three numeric letters in ten bits.
                val num2 = content[i + 1].code - '0'.code
                val num3 = content[i + 2].code - '0'.code
                bits.appendBits(num1 * 100 + num2 * 10 + num3, 10)
                i += 3
            } else if (i + 1 < length) {
                // Encode two numeric letters in seven bits.
                val num2 = content[i + 1].code - '0'.code
                bits.appendBits(num1 * 10 + num2, 7)
                i += 2
            } else {
                // Encode one numeric letter in four bits.
                bits.appendBits(num1, 4)
                i++
            }
        }
    }

    @Throws(WriterException::class)
    fun append8BitBytes(content: String, bits: BitArray, encoding: String?) {
        val bytes: ByteArray = try {
            content.toByteArray(charset(encoding!!))
        } catch (uee: UnsupportedEncodingException) {
            throw WriterException(uee)
        }
        for (b in bytes) {
            bits.appendBits(b.toInt(), 8)
        }
    }

    @Throws(WriterException::class)
    fun appendKanjiBytes(content: String, bits: BitArray) {
        val bytes: ByteArray = try {
            content.toByteArray(charset("Shift_JIS"))
        } catch (uee: UnsupportedEncodingException) {
            throw WriterException(uee)
        }
        val length = bytes.size
        var i = 0
        while (i < length) {
            val byte1 = bytes[i].toInt() and 0xFF
            val byte2 = bytes[i + 1].toInt() and 0xFF
            val code = byte1 shl 8 or byte2
            var subtracted = -1
            if (code in 0x8140..0x9ffc) {
                subtracted = code - 0x8140
            } else if (code in 0xe040..0xebbf) {
                subtracted = code - 0xc140
            }
            if (subtracted == -1) {
                throw WriterException("Invalid byte sequence")
            }
            val encoded = (subtracted shr 8) * 0xc0 + (subtracted and 0xff)
            bits.appendBits(encoded, 13)
            i += 2
        }
    }

    @Throws(WriterException::class)
    fun appendAlphanumericBytes(content: CharSequence, bits: BitArray) {
        val length = content.length
        var i = 0
        while (i < length) {
            val code1 = getAlphanumericCode(content[i].code)
            if (code1 == -1) {
                throw WriterException()
            }
            if (i + 1 < length) {
                val code2 = getAlphanumericCode(content[i + 1].code)
                if (code2 == -1) {
                    throw WriterException()
                }
                // Encode two alphanumeric letters in 11 bits.
                bits.appendBits(code1 * 45 + code2, 11)
                i += 2
            } else {
                // Encode one alphanumeric letter in six bits.
                bits.appendBits(code1, 6)
                i++
            }
        }
    }

    private fun appendECI(eci: CharacterSetECI, bits: BitArray) {
        bits.appendBits(Mode.ECI.bits, 4)
        // This is correct for values up to 127, which is all we need now.
        bits.appendBits(eci.value, 8)
    }

    private fun appendModeInfo(mode: Mode, bits: BitArray) {
        bits.appendBits(mode.bits, 4)
    }
}