package blockchain

class TransactionSigned private constructor(val id: Int, val sender: String, val receiver: String, val amount: Int, val signature: ByteArray ) {

    override fun toString(): String {
        return "$sender sent $amount VC to $receiver"
    }


    companion object {
        fun newInstance(transactionProposed: TransactionProposed, signature: ByteArray): TransactionSigned {
            return TransactionSigned(
                transactionProposed.id,
                transactionProposed.sender,
                transactionProposed.receiver,
                transactionProposed.amount,
                signature)
        }
    }
}


















