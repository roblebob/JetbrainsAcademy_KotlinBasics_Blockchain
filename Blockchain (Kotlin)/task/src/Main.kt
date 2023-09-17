package blockchain

import java.security.MessageDigest

val T_MIN = 100
val T_MAX = 1000
val N_MINERS = 10

val NAMES = listOf("Tom", "Sarah", "Nick", "John", "Mary", "Alex", "Steve", "Anna")

val MESSAGES = listOf(
    "Hello! How are you?",
    "It's not fair!\nYou always will be first because it is your blockchain!",
    "I'm the best programmer in the world!",
    "Anyway, thank you for this amazing chat."
)


fun main() {


    val miners = mutableListOf<Miner>()
    for (i in 1..N_MINERS) {
        miners.add(Miner(i))
    }
    miners.forEach {
        it.start()
    }
    miners.forEach { it.join() }
    Blockchain.print()
    println(Blockchain.validateAll())
}



class Miner(val id: Int) : Thread() {


    override fun run() {

        do {
            val block = Block(Blockchain.size + 1 ,
                Blockchain.getLastHash(),
                Blockchain.nZeros,
                id,
                Blockchain.getMessages(),
                Blockchain.getLastTime()
            )
        } while (!Blockchain.validateAndAdd( block))

        // ... so first message is definitely added after first block was created
        try {
            Blockchain.addMessages("${NAMES.random()}: ${MESSAGES.random()}}")
        } catch (e: Exception) {
            println("----->" + e.message)
        }
    }
}







object Blockchain {

    @Volatile
    var nZeros: Int = 0

    private val chain : MutableList<Block> = mutableListOf()
    private val messages : MutableList<String> = mutableListOf()


    fun getMessages(): MutableList<String> {
        return messages
    }

    @Synchronized
    fun addMessages(message: String) {
        messages.add(message)
    }

    @Synchronized
    fun removeMessages(message: List<String>) {
        messages.removeAll(message)
    }




    @get:Synchronized
    val size: Int
        get() = chain.size

    @Synchronized
    fun getLastHash(): String {
        return if (chain.isEmpty()) "0" else chain.last().hash
    }

    @Synchronized
    fun getLastTime() : Long {
        return if (chain.isEmpty()) System.currentTimeMillis() else chain.last().tAfter
    }


    @Synchronized
    fun validateAndAdd(candidate: Block, ): Boolean {
        if (((chain.isEmpty() && candidate.previousHash == "0") ||
            chain.last().hash == candidate.previousHash)
            &&
            candidate.hash.substring(0, nZeros) == "0".repeat(nZeros) )
        {
            chain.add(candidate)
            when {
                chain.last().forDuration < T_MIN -> nZeros++
                chain.last().forDuration > T_MAX -> nZeros--
            }
            print()
            removeMessages(candidate.messages)
            return true
        }

        return false
    }



    @Synchronized
    fun validateAll(): Boolean {
        when (chain.size) {
            0 -> return true
            1 -> return  chain[0].hash.substring(0, chain[0].nZeros) == "0".repeat(chain[0].nZeros) &&
                    chain[0].previousHash == "0"
            else -> {
                for (i in 1..chain.lastIndex) {
                    if (chain[i - 1].hash != chain[i].previousHash) return false
                    if (chain[i].hash.substring(0, chain[i].nZeros) != "0".repeat(chain[i].nZeros)) return false
                }
                return true
            }
        }
    }











@Synchronized
    fun print() {

        val o = chain.last()

        println("""Block: 
Created by miner # ${o.minerId}
Id: ${o.id}
Timestamp: ${o.t0}
Magic number: ${o.magicNumber}
Hash of the previous block:
${o.previousHash}
Hash of the block:
${o.hash}
Block data: ${if (o.messages.isEmpty()) "no messages" else "\n" + o.messages.joinToString("\n")}
Block was generating for ${o.forDuration} seconds   (${o.nTrials} attempts)
${  when (nZeros - o.nZeros) {
        1 -> "N was increased to $nZeros"
        -1 -> "N was decreased by 1 ($nZeros)"
        else -> "N stays the same ($nZeros)"
    }}
""")
    }
}







class Block(val id: Int,
            val previousHash: String,
            val nZeros: Int = 5,
            val minerId: Int = 0,
            val messages: MutableList<String> = mutableListOf(),
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

    init {
        do {
            magicNumber = (0..Int.MAX_VALUE).random()
            nTrials++
            tAfter = System.currentTimeMillis()

        } while (hash.substring(0, nZeros) != "0".repeat(nZeros))
    }


    override fun toString(): String {
        return "Block(id=$id, previousHash='$previousHash', nZeros=$nZeros, minerId=$minerId, messages=${messages.joinToString("\n") ?: ""}, tBefore=$tBefore, t0=$t0, tAfter=$tAfter, nTrials=$nTrials, magicNumber=$magicNumber)"
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