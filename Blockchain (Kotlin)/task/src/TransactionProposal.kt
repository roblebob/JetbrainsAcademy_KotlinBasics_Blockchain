package blockchain

class TransactionProposal private constructor(val id: Int, val sender: String, val receiver: String, val amount: Int) {

    override fun toString(): String {
        return "TransactionProposed(id=$id, sender='$sender', receiver='$receiver', amount=$amount)"
    }

    companion object {
        @Volatile
        private var total = 0
        fun newInstance(sender: String, receiver: String, amount: Int): TransactionProposal {
            return TransactionProposal(++total, sender, receiver, amount)
        }
    }
}
