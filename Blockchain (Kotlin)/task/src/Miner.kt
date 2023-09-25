package blockchain

import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature




class Miner(val id: Int, val publicKey: PublicKey, private val privateKey: PrivateKey) : Thread() {
    init {
        name = "miner$id"
        Blockchain.addMinersPublicKey(name, publicKey)
        Blockchain.initLedgerEntry(name)
    }

    override fun run() {
        do {
            do {
                val ledgerTemp = Blockchain.getLedgerCopy()
                val transactionsSigned: MutableList<TransactionSigned> = mutableListOf()
                for (transactionProposed in Blockchain.getTransactionsProposed()) {

                    // checking if the transaction is valid, and remove those that are not

                    val sender = transactionProposed.sender
                    val receiver = transactionProposed.receiver
                    val amount = transactionProposed.amount

                    if (sender == receiver) continue
                    if (ledgerTemp[sender]!! < amount) continue

                    // updating the temporäry ledger
                    ledgerTemp[sender] = ledgerTemp[sender]!! - amount
                    ledgerTemp[receiver] = ledgerTemp[receiver]!! + amount

                    // signing the transaction
                    val rsa = Signature.getInstance("SHA1withRSA")
                    rsa.initSign(privateKey)
                    rsa.update(transactionProposed.toString().toByteArray())
                    transactionsSigned.add(TransactionSigned(transactionProposed, rsa.sign()))
                }

                val block = Block(
                    Blockchain.getNewId(),
                    Blockchain.getLastHash(),
                    Blockchain.getLeadingZeros(),
                    name,
                    transactionsSigned,
                    Blockchain.getLastTime()
                )

                if (N_BLOCKS <= Blockchain.size()) break
            } while (!Blockchain.validateAndAdd(block))
        } while (N_BLOCKS > Blockchain.size())
    }






    companion object {
        private val keyGen: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
        private var total = 0

        init {
            keyGen.initialize(KEY_LENGTH)
        }

        fun newInstance() : Miner {
            val pair = keyGen.generateKeyPair()
            return Miner(++total, pair.public, pair.private)
        }
    }
}

