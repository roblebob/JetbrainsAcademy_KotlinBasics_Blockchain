package blockchain

import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature




class Miner(val id: Int, val publicKey: PublicKey, private val privateKey: PrivateKey) : Thread() {
    init {
        name = "miner$id"
        Blockchain.addMinersPublicKey(name, publicKey)
    }

    override fun run() {
        do {
            do {
                val transactionsSigned: MutableList<TransactionSigned> = mutableListOf()
                for (transactionProposed in Blockchain.getTransactionsProposed()) {

                    val rsa = Signature.getInstance("SHA1withRSA")
                    rsa.initSign(privateKey)
                    rsa.update(transactionProposed.toString().toByteArray())
                    transactionsSigned.add(TransactionSigned.newInstance(transactionProposed, rsa.sign()))
                }

                val block = Block(
                    Blockchain.getNewId(),
                    Blockchain.getLastHash(),
                    Blockchain.getLeadingZeros(),
                    name,
                    transactionsSigned,
                    Blockchain.getLastTime()
                )

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

