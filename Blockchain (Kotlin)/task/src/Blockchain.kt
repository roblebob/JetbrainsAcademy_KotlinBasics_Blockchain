package blockchain

import java.security.PublicKey
import java.security.Signature

val NAMES = mutableSetOf("Tom", "Sarah", "Nick", "John", "Mary", "Alex", "Steve", "Anna")


object Blockchain {
    private val chain: MutableList<Block> = mutableListOf()
    private val transactionsProposed: MutableList<TransactionProposed> = mutableListOf()
    private val minersPublicKeys: MutableMap<String, PublicKey> = mutableMapOf()
    private val ledger: MutableMap<String, Int> = mutableMapOf()

    init {
        for (name in NAMES) {
            ledger[name] = 0
        }
    }


    fun size() = chain.size
    fun getNewId() = chain.size + 1
    fun getTransactionsProposed(): MutableList<TransactionProposed> = transactionsProposed.toMutableList()
    fun getLastHash() =  if (chain.isEmpty()) "0" else chain.last().hash
    fun getLastTime(): Long = if (chain.isEmpty()) System.currentTimeMillis() else chain.last().tAfter
    fun getLeadingZeros(): Int = if (chain.isEmpty()) 0 else chain.last().futureLeadingZeros



    @Synchronized
    fun addMinersPublicKey(name: String, publicKey: PublicKey) {
        minersPublicKeys[name] = publicKey
    }


    @Synchronized
    fun generateTransactionsProposed() {

        val ledgerTemp = ledger.toMutableMap()

        repeat((1..10).random()) {

            val sender = ledgerTemp.filter { it.value > 0 }.keys.random()
            if (sender.isEmpty()) return

            val receiver = ledgerTemp.filter { it.key != sender }.keys.random()
            if (receiver.isEmpty()) return

            val amount = (1..ledgerTemp[sender]!!).random()

            transactionsProposed.add(TransactionProposed.newInstance(sender, receiver, amount))

            ledgerTemp[sender]   = ledgerTemp[sender]!!   - amount
            ledgerTemp[receiver] = ledgerTemp[receiver]!! + amount
        }
    }




    @Synchronized
    fun addTransactionProposal(transactionProposed: TransactionProposed) {
        val sender = transactionProposed.sender
        val receiver = transactionProposed.receiver

        if (ledger.containsKey(sender)) {

            // abort if sender has not enough money
            if (ledger[sender]!! < transactionProposed.amount) {
                println("Not enough money")
                return
            }

            // else update ledger
            ledger[sender] = ledger[sender]!! - transactionProposed.amount
            ledger[receiver] = if (ledger.containsKey(receiver)) ledger[receiver]!! + transactionProposed.amount else transactionProposed.amount

        } else {
            println("Sender not found")
            return
        }



        val sig = Signature.getInstance("SHA1withRSA")
        sig.initVerify(publicKey)
        sig.update(transaction.text.toByteArray())
        if (sig.verify(transaction.signature))
            transactions.add(transaction)
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
            candidate.hash.substring(0, candidate.leadingZeros) == "0".repeat(candidate.leadingZeros)

        )
        {
            chain.add( candidate)





            ledger[sender] = ledger[sender]!! - transactionProposed.amount
            ledger[receiver] = if (ledger.containsKey(receiver)) ledger[receiver]!! + transactionProposed.amount else transactionProposed.amount


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
            1 -> return  chain[0].hash.substring(0, chain[0].leadingZeros) == "0".repeat(chain[0].leadingZeros) &&
                    chain[0].previousHash == "0"
            else -> {
                for (i in 1..chain.lastIndex) {
                    if (chain[i - 1].hash != chain[i].previousHash) return false
                    if (chain[i].hash.substring(0, chain[i].leadingZeros) != "0".repeat(chain[i].leadingZeros)) return false
                }
                return true
            }
        }
    }


    @Synchronized
    fun print() {

        val o = chain.last()

        println("""Block: 
Created by miner: ${o.minerName}
${o.minerName} gets 100 VC
Id: ${o.id}
Timestamp: ${o.t0}
Magic number: ${o.magicNumber}
Hash of the previous block:
${o.previousHash}
Hash of the block:
${o.hash}
Block data: ${if (o.transactionsSigned.isEmpty()) "no transactions" else "\n" + o.transactionsSigned.joinToString("\n")}
Block was generating for ${o.forDuration} seconds   (${o.nTrials} attempts)
${  when (o.futureLeadingZeros - o.leadingZeros) {
            1 -> "N was increased to ${o.futureLeadingZeros}"
            -1 -> "N was decreased by 1 (${o.futureLeadingZeros})"
            else -> "N stays the same (${o.futureLeadingZeros})"
        }}
""")
    }
}


