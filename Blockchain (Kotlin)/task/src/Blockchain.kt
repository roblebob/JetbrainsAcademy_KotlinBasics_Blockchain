package blockchain

import java.security.PublicKey
import java.security.Signature

val NAMES = mutableSetOf("Blockchain", "Tom", "Sarah", "Nick", "John", /* "Mary", "Alex", "Steve", "Anna" */ )


object Blockchain {
    private val _chain: MutableList<Block> = mutableListOf()
    private val _transactionProposals: MutableList<TransactionProposal> = mutableListOf()
    private val _minersPublicKeys: MutableMap<String, PublicKey> = mutableMapOf()
    private val _ledger: MutableMap<String, Int> = mutableMapOf()

    fun initLedgerEntry(name: String) {
        _ledger[name] = 0
    }

    init {
        for (name in NAMES) {
            _ledger[name] = 0
        }
    }


    fun size() = _chain.size
    fun getNewId() = _chain.size + 1
    fun getTransactionsProposed(): MutableList<TransactionProposal> = _transactionProposals.toMutableList()
    fun getLastHash() =  if (_chain.isEmpty()) "0" else _chain.last().hash
    fun getLastTime(): Long = if (_chain.isEmpty()) System.currentTimeMillis() else _chain.last().tAfter
    fun getLeadingZeros(): Int = if (_chain.isEmpty()) 0 else _chain.last().futureLeadingZeros
    fun getLedgerCopy(): MutableMap<String, Int> = _ledger.toMutableMap()


    @Synchronized
    fun addMinersPublicKey(name: String, publicKey: PublicKey) {
        _minersPublicKeys[name] = publicKey
    }




    @Synchronized
    fun generateTransactionsProposals() {

        val ledgerTemp = _ledger.toMutableMap()

        repeat((1..10).random()) {


            val sender = ledgerTemp.filter { it.value > 0 }.keys.random()
            if (sender.isEmpty()) return

            val receiver = ledgerTemp.filter { it.key != sender }.keys.random()
            if (receiver.isEmpty()) return

            val amount = (1..ledgerTemp[sender]!!).random()

            _transactionProposals.add(TransactionProposal.newInstance(sender, receiver, amount))

            ledgerTemp[sender]   = ledgerTemp[sender]!!   - amount
            ledgerTemp[receiver] = ledgerTemp[receiver]!! + amount
        }
    }





    @Synchronized
    fun validateAndAdd(candidate: Block): Boolean {
        if (
            candidate.id == _chain.size + 1
            &&
            (
                    (_chain.isEmpty() && candidate.previousHash == "0")
                            || _chain.last().hash == candidate.previousHash
            )
            &&
            candidate.hash.substring(0, candidate.leadingZeros) == "0".repeat(candidate.leadingZeros)

        )
        {
            //println("start validating  $candidate")
            for (transactionSigned in candidate.transactionsSigned) {
                val sender = transactionSigned.proposal.sender
                val receiver = transactionSigned.proposal.receiver
                val amount = transactionSigned.proposal.amount
                val miner = candidate.minerName

                // abort if the transaction is invalid
                if (sender == receiver  ||  _ledger[sender]!! < amount) {
                    _transactionProposals.remove(transactionSigned.proposal)
                    println("aborted, because the transaction is invalid")
                    return false
                }

                // checking the signature
                val rsa = Signature.getInstance("SHA1withRSA")
                rsa.initVerify(_minersPublicKeys[miner])
                rsa.update(transactionSigned.proposal.toString().toByteArray())
                if (!rsa.verify(transactionSigned.signature)) {
                    println("aborted, because the signature is invalid")
                    return false
                }

                // execute transaction
                _ledger[sender] = _ledger[sender]!! - amount
                _ledger[receiver] = _ledger[receiver]!! + amount
                _transactionProposals.remove(transactionSigned.proposal)
            }

            _chain.add( candidate)
            _ledger[ candidate.minerName] = _ledger[ candidate.minerName]!! + 100
            print()
            generateTransactionsProposals()
            return true
        }

        return false
    }












    @Synchronized
    fun validateAll(): Boolean {
        // TODO: addidional checks:
        //  - increasing transaction id,
        //  - increasing block id,
        //  - increasing  tBefore, t0, tAfter,
        when (_chain.size) {
            0 -> return true
            1 -> return  _chain[0].hash.substring(0, _chain[0].leadingZeros) == "0".repeat(_chain[0].leadingZeros) &&
                    _chain[0].previousHash == "0"
            else -> {
                for (i in 1.._chain.lastIndex) {
                    if (_chain[i - 1].hash != _chain[i].previousHash) return false
                    if (_chain[i].hash.substring(0, _chain[i].leadingZeros) != "0".repeat(_chain[i].leadingZeros)) return false
                }
                return true
            }
        }
    }


    @Synchronized
    fun print() {

        val o = _chain.last()

        println("""Block: 
Created by miner: ${o.minerName}
${o.minerName} gets 100 VC   (${_ledger.entries.joinToString(" ")})
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


