package blockchain

import java.security.MessageDigest

fun main() {

    val blockchain = Blockchain()
    repeat(5) {
        blockchain.generate()
    }
    blockchain.chain.forEach {
        println("Block:")
        println("Id: ${it.id}")
        println("Timestamp: ${it.timestamp}")
        println("Hash of the previous block:")
        println(it.previousHash)
        println("Hash of the block:")
        println(it.hash)
        println()
    }
    blockchain.validate()
}




class Blockchain {

    val chain : MutableList<Block> = mutableListOf()

    fun generate() {
        val block = Block( chain.size + 1 , if (chain.isEmpty()) "0"  else chain.last().hash)
        chain.add( block)
    }

    fun validate(): Boolean {
        for (i in chain.size - 1 downTo 1) {
            if (chain[i].previousHash != chain[i - 1].hash) {
                return false
            }
        }
        return true
    }

    class Block(val id: Int, val previousHash: String) {
        val timestamp: Long
        val hash: String
            get() = applySha256("$id$timestamp$previousHash")
        init {
            this.timestamp  = System.currentTimeMillis()
        }
    }
}




fun applySha256(input: String): String {
    return try {
        val digest = MessageDigest.getInstance("SHA-256")
        /* Applies sha256 to our input */
        val hash = digest.digest(input.toByteArray(charset("UTF-8")))
        val hexString = StringBuilder()
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