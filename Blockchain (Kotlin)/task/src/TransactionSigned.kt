package blockchain

class TransactionSigned(val proposal: TransactionProposal, val signature: ByteArray) {

    override fun toString(): String {
        return "${proposal.sender} sent ${proposal.amount} VC to ${proposal.receiver}"
    }




}


















