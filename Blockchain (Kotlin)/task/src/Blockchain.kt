package blockchain

import java.security.PublicKey
import java.security.Signature


object Blockchain {
    private val chain : MutableList<Block> = mutableListOf()
    private val messages : MutableList<Myssage> = mutableListOf()
    private val minersPublicKeys : MutableMap<String, PublicKey> = mutableMapOf()

    @Synchronized
    fun addMinersPublicKey(name: String, publicKey: PublicKey) {
        minersPublicKeys[name] = publicKey
    }
    fun getMessages(): MutableList<Myssage> {
        return messages.toMutableList()
    }

    @Synchronized
    fun addMessage(message: Myssage) {
        val name = message.name
        val publicKey = minersPublicKeys[name]
        val sig = Signature.getInstance("SHA1withRSA")
        sig.initVerify(publicKey)
        sig.update(message.text.toByteArray())
        if (sig.verify(message.signature))
            messages.add(message)
    }


    fun getNewId(): Int {
        return chain.size + 1
    }




    fun getLastHash(): String {
        return if (chain.isEmpty()) "0" else chain.last().hash
    }


    fun getLastTime() : Long {
        return if (chain.isEmpty()) System.currentTimeMillis() else chain.last().tAfter
    }


    fun getNZeros(): Int {
        return if (chain.isEmpty()) 0 else chain.last().futureNZeros
    }


    @Synchronized
    fun validateAndAdd(candidate: Block): Boolean {
        if (
            candidate.id == chain.size + 1
            &&
            (
                    (chain.isEmpty() && candidate.previousHash == "0")
                            || chain.last().hash == candidate.previousHash
                    )
            &&
            candidate.hash.substring(0, candidate.nZeros) == "0".repeat(candidate.nZeros)

        )
        {
            chain.add(candidate)
            print()
            messages.removeAll(candidate.messages)
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
Created by miner # ${o.minerName}
Id: ${o.id}
Timestamp: ${o.t0}
Magic number: ${o.magicNumber}
Hash of the previous block:
${o.previousHash}
Hash of the block:
${o.hash}
Block data: ${if (o.messages.isEmpty()) "no messages" else "\n" + o.messages.joinToString("\n")}
Block was generating for ${o.forDuration} seconds   (${o.nTrials} attempts)
${  when (o.futureNZeros - o.nZeros) {
            1 -> "N was increased to ${o.futureNZeros}"
            -1 -> "N was decreased by 1 (${o.futureNZeros})"
            else -> "N stays the same (${o.futureNZeros})"
        }}
""")
    }
}


