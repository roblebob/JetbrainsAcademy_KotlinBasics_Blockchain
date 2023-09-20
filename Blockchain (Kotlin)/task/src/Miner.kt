package blockchain

import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

val NAMES = mutableSetOf("Tom", "Sarah", "Nick", "John", "Mary", "Alex", "Steve", "Anna")

val KEY_LENGTH = 1024

class Miner(val id: Int,  val publicKey: PublicKey, val privateKey: PrivateKey) : Thread() {

    private val _name: String = NAMES.random()



    init {
        NAMES.remove(_name)
        Blockchain.addMinersPublicKey(mName, publicKey)
    }

    val mName: String
        get() = "$id:$_name"


    override fun run() {

        do {
            val block = Block(
                Blockchain.getNewId() ,
                Blockchain.getLastHash(),
                Blockchain.getNZeros(),
                mName,
                Blockchain.getMessages(),
                Blockchain.getLastTime()
            )
        } while (!Blockchain.validateAndAdd( block))

        // ... so first message is definitely added after the first block was created
        try {
            val text = MESSAGES.random()

            val rsa = Signature.getInstance("SHA1withRSA")
            rsa.initSign(privateKey)
            rsa.update(text.toByteArray())
            Blockchain.addMessage(Myssage.newInstance( mName, text, rsa.sign()))
        } catch (e: Exception) {
            println("----->" + e.message)
        }
    }






    companion object {
        val keyGen: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
        var total = 0

        init {
            keyGen.initialize(KEY_LENGTH)
        }

        fun getInstances(id: Int) : Miner {
            val pair = keyGen.generateKeyPair()
            return Miner(++total, pair.public, pair.private)
        }

    }

}

