package blockchain

import java.security.MessageDigest


class Block(val id: Int,
            val previousHash: String,
            val nZeros: Int = 5,
            val minerName: String,
            val messages: MutableList<Myssage> = mutableListOf(),
            val tBefore: Long,
) {
    val t0: Long = System.currentTimeMillis()

    var tAfter: Long
    var nTrials: Int = 0
    var magicNumber: Int



    val hash: String
        get() = applySha256(toString())

    val forDuration: Long
        get() = tAfter - tBefore

    val futureNZeros: Int
        get() =  when {
            forDuration < T_MIN -> nZeros + 1
            forDuration > T_MAX -> nZeros - 1
            else -> nZeros
        }


    init {
        do {
            magicNumber = (0..Int.MAX_VALUE).random()
            nTrials++
            tAfter = System.currentTimeMillis()


        } while (hash.substring(0, nZeros) != "0".repeat(nZeros))
    }


    override fun toString(): String {
        return "Block(id=$id, previousHash='$previousHash', nZeros=$nZeros, miner=$minerName, messages=${messages.joinToString("\n") ?: ""}, tBefore=$tBefore, t0=$t0, tAfter=$tAfter, nTrials=$nTrials, magicNumber=$magicNumber)"
    }


}





fun applySha256(input: String): String {
    return try {
        val hexString = StringBuilder()
        val digest = MessageDigest.getInstance("SHA-256")
        /* Applies sha256 to our input */
        val hash: ByteArray = MessageDigest.getInstance("SHA-256").digest(input.toByteArray(charset("UTF-8")))
        for (elem in hash) {
            val hex = Integer.toHexString(0xff and elem.toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        hexString.toString()
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}